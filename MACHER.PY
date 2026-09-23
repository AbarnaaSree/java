"""
AI-assisted skill matching for JobMon.

Design goals:
- Exact matches are deterministic.
- AI identifies conservative semantic relationships.
- AI cannot invent resume or JD skills.
- Every accepted AI match must reference supplied data.
- Weak or unsupported relationships remain missing.
- Required and preferred skills use one AI call.
- Deterministic matching remains available if AI is unavailable.
"""

from __future__ import annotations

import json
import re
from typing import Any


# ---------------------------------------------------------------------------
# Basic normalization
# ---------------------------------------------------------------------------

def _normalize(value: str) -> str:
    """Normalize a value for comparison."""

    value = str(value).strip().lower()

    value = re.sub(
        r"[\u2010-\u2015]",
        "-",
        value,
    )

    value = re.sub(
        r"[^a-z0-9+#.\- ]+",
        " ",
        value,
    )

    value = re.sub(
        r"\s+",
        " ",
        value,
    )

    return value.strip()


def _dedupe(values: list[str]) -> list[str]:
    """Remove duplicates while preserving order."""

    result: list[str] = []
    seen: set[str] = set()

    for value in values:
        if not isinstance(value, str):
            continue

        value = value.strip()

        if not value:
            continue

        key = _normalize(value)

        if key in seen:
            continue

        seen.add(key)
        result.append(value)

    return result


# ---------------------------------------------------------------------------
# Deterministic matching
# ---------------------------------------------------------------------------

def _is_exact_match(
    resume_skill: str,
    jd_skill: str,
) -> bool:
    """Return True when two skills normalize to the same value."""

    return _normalize(resume_skill) == _normalize(jd_skill)


def _is_deterministically_related(
    resume_skill: str,
    jd_skill: str,
) -> bool:
    """
    Identify only unambiguous technical variants.

    Broader semantic relationships are delegated to AI.
    """

    resume = _normalize(resume_skill)
    jd = _normalize(jd_skill)

    if not resume or not jd:
        return False

    # Equivalent database naming variants.
    database_variants = {
        "postgres": "postgresql",
        "postgresql": "postgresql",
        "sql server": "microsoft sql server",
        "microsoft sql server": "microsoft sql server",
    }

    if (
        resume in database_variants
        and jd in database_variants
        and database_variants[resume]
        == database_variants[jd]
    ):
        return True

    # Equivalent language naming variants.
    language_variants = {
        "python": "python",
        "python 3": "python",
        "python3": "python",
        "javascript": "javascript",
        "js": "javascript",
        "typescript": "typescript",
        "ts": "typescript",
    }

    if (
        resume in language_variants
        and jd in language_variants
        and language_variants[resume]
        == language_variants[jd]
    ):
        return True

    # Explicit product/service naming variants.
    equivalent_pairs = {
        frozenset({"power bi", "power bi service"}),
        frozenset({"azure ai search", "ai search"}),
        frozenset({"postgres", "postgresql"}),
        frozenset({"sql server", "microsoft sql server"}),
    }

    if frozenset({resume, jd}) in equivalent_pairs:
        return True

    return False


# ---------------------------------------------------------------------------
# JSON handling
# ---------------------------------------------------------------------------

def _parse_json(text: str) -> dict[str, Any]:
    """Safely parse a JSON object returned by the AI provider."""

    if not isinstance(text, str):
        return {}

    text = text.strip()

    if not text:
        return {}

    text = re.sub(
        r"^```(?:json)?\s*",
        "",
        text,
        flags=re.IGNORECASE,
    )

    text = re.sub(
        r"\s*```$",
        "",
        text,
        flags=re.IGNORECASE,
    )

    text = text.strip()

    try:
        parsed = json.loads(text)

        if isinstance(parsed, dict):
            return parsed

    except json.JSONDecodeError:
        pass

    start = text.find("{")
    end = text.rfind("}")

    if start >= 0 and end > start:
        try:
            parsed = json.loads(
                text[start : end + 1]
            )

            if isinstance(parsed, dict):
                return parsed

        except json.JSONDecodeError:
            pass

    return {}


# ---------------------------------------------------------------------------
# AI matching
# ---------------------------------------------------------------------------

def _ai_semantic_match(
    provider: Any,
    resume_skills: list[str],
    required_skills: list[str],
    preferred_skills: list[str],
) -> dict[str, Any]:
    """
    Ask the AI provider to identify semantic skill relationships.

    Exactly one provider call is made.
    """

    if provider is None:
        return {}

    payload = {
        "resume_skills": resume_skills,
        "required_skills": required_skills,
        "preferred_skills": preferred_skills,
    }

    system_prompt = """
You are a strict resume-to-job skill matching system.

Compare ONLY the supplied lists.

SOURCE RESTRICTIONS:
- You may use only resume_skills as evidence for the resume.
- You may use only required_skills and preferred_skills as JD skills.
- Never invent a resume skill.
- Never invent a JD skill.
- Never add skills from your own knowledge.
- Never use information outside the supplied lists.

MATCHING RULES:

1. exact
Use exact when the two supplied skills represent the same skill.

2. related
Use related only when there is strong, direct technical evidence
that the supplied resume skill substantially represents the supplied
JD skill.

3. partial
Use partial only when there is meaningful overlap but the resume
skill does not fully represent the JD skill.

4. missing
Use missing when the resume does not provide sufficient evidence.

IMPORTANT:
Do not treat general field membership as a skill match.

For example, a programming language does not automatically prove
knowledge of mathematics.

A machine-learning library does not automatically prove every
mathematical, statistical, data-mining, pattern-recognition, or
predictive-modeling skill.

Do not create matches merely because two concepts are commonly used
together.

Prefer missing when the evidence is uncertain.

OUTPUT:
Return JSON only.

Every supplied required skill must appear exactly once.
Every supplied preferred skill must appear exactly once.

For a matched skill:
- resume_skill must be copied exactly from resume_skills.
- jd_skill must be copied exactly from the corresponding JD list.

For a missing skill:
- resume_skill must be an empty string.
- jd_skill must be copied exactly from the corresponding JD list.

confidence must be a number from 0 to 1.
""".strip()

    user_prompt = f"""
Resume skills:

{json.dumps(resume_skills, indent=2, ensure_ascii=False)}

Required JD skills:

{json.dumps(required_skills, indent=2, ensure_ascii=False)}

Preferred JD skills:

{json.dumps(preferred_skills, indent=2, ensure_ascii=False)}

Return exactly:

{{
  "required_matches": [
    {{
      "resume_skill": "",
      "jd_skill": "",
      "match_type": "exact",
      "confidence": 0.0,
      "reason": ""
    }}
  ],
  "preferred_matches": [
    {{
      "resume_skill": "",
      "jd_skill": "",
      "match_type": "exact",
      "confidence": 0.0,
      "reason": ""
    }}
  ]
}}
""".strip()

    try:
        response = provider.generate(
            user_prompt,
            system_prompt=system_prompt,
        )


        return _parse_json(response)

    except Exception:
        return {}


# ---------------------------------------------------------------------------
# AI output extraction
# ---------------------------------------------------------------------------

def _collect_ai_matches(
    ai_result: dict[str, Any],
) -> list[dict[str, Any]]:
    """Collect required and preferred AI matches."""

    if not isinstance(ai_result, dict):
        return []

    result: list[dict[str, Any]] = []

    # Expected structure.
    for key in (
        "required_matches",
        "preferred_matches",
    ):
        values = ai_result.get(key)

        if isinstance(values, list):
            for value in values:
                if isinstance(value, dict):
                    result.append(value)

    # Also support a generic matches array if returned by the provider.
    values = ai_result.get("matches")

    if isinstance(values, list):
        for value in values:
            if isinstance(value, dict):
                result.append(value)

    return result


def _validate_ai_matches(
    ai_result: dict[str, Any],
    resume_skills: list[str],
    required_skills: list[str],
    preferred_skills: list[str],
) -> list[dict[str, Any]]:
    """
    Validate AI output against the original supplied skill lists.
    """

    if not isinstance(ai_result, dict):
        return []

    resume_lookup = {
        _normalize(skill): skill
        for skill in resume_skills
    }

    required_lookup = {
        _normalize(skill): skill
        for skill in required_skills
    }

    preferred_lookup = {
        _normalize(skill): skill
        for skill in preferred_skills
    }

    all_jd_lookup = {
        **required_lookup,
        **preferred_lookup,
    }

    validated: list[dict[str, Any]] = []

    for item in _collect_ai_matches(ai_result):
        raw_resume = item.get(
            "resume_skill",
            "",
        )

        raw_jd = item.get(
            "jd_skill",
            "",
        )

        if not isinstance(raw_resume, str):
            continue

        if not isinstance(raw_jd, str):
            continue

        resume_key = _normalize(raw_resume)
        jd_key = _normalize(raw_jd)

        # JD skill must always come from supplied data.
        if jd_key not in all_jd_lookup:
            continue

        canonical_jd = all_jd_lookup[jd_key]

        # Missing relationship.
        if not resume_key:
            validated.append(
                {
                    "resume_skill": "",
                    "jd_skill": canonical_jd,
                    "match_type": "missing",
                    "confidence": 1.0,
                    "reason": (
                        "No sufficiently supported resume skill."
                    ),
                }
            )
            continue

        # Resume skill must come from supplied data.
        if resume_key not in resume_lookup:
            continue

        canonical_resume = resume_lookup[resume_key]

        match_type = str(
            item.get(
                "match_type",
                "",
            )
        ).strip().lower()

        if match_type not in {
            "exact",
            "related",
            "partial",
            "missing",
        }:
            continue

        if match_type == "missing":
            continue

        try:
            confidence = float(
                item.get(
                    "confidence",
                    0,
                )
            )
        except (
            TypeError,
            ValueError,
        ):
            confidence = 0.0

        confidence = max(
            0.0,
            min(
                1.0,
                confidence,
            ),
        )

        reason = item.get(
            "reason",
            "",
        )

        if not isinstance(reason, str):
            reason = ""

        # Deterministic exact matching always wins.
        if _is_exact_match(
            canonical_resume,
            canonical_jd,
        ):
            match_type = "exact"
            confidence = 1.0
            reason = "Exact normalized skill match."

        # Deterministic equivalent relationship.
        elif _is_deterministically_related(
            canonical_resume,
            canonical_jd,
        ):
            match_type = "related"
            confidence = max(
                confidence,
                0.95,
            )
            reason = (
                "Strong equivalent or naming relationship."
            )

        # AI confidence is advisory; the relationship itself
        # must still reference supplied resume and JD skills.
        elif match_type in {
            "related",
            "partial",
        }:
            if confidence <= 0:
                continue

        validated.append(
            {
                "resume_skill": canonical_resume,
                "jd_skill": canonical_jd,
                "match_type": match_type,
                "confidence": confidence,
                "reason": reason.strip(),
            }
        )

    return validated


# ---------------------------------------------------------------------------
# Category matching
# ---------------------------------------------------------------------------

def _match_category(
    resume_skills: list[str],
    jd_skills: list[str],
    ai_matches: list[dict[str, Any]],
) -> dict[str, Any]:
    """Match one category of JD skills."""

    resume_skills = _dedupe(
        resume_skills
    )

    jd_skills = _dedupe(
        jd_skills
    )

    resume_lookup = {
        _normalize(skill): skill
        for skill in resume_skills
    }

    jd_lookup = {
        _normalize(skill): skill
        for skill in jd_skills
    }

    exact_matches: list[str] = []
    related_matches: list[str] = []
    partial_matches: list[str] = []
    missing_matches: list[str] = []

    details: list[dict[str, Any]] = []

    matched_jd_keys: set[str] = set()

    # ---------------------------------------------------------------
    # Exact matching
    # ---------------------------------------------------------------

    for jd_key, jd_skill in jd_lookup.items():
        resume_skill = resume_lookup.get(
            jd_key
        )

        if resume_skill:
            exact_matches.append(
                jd_skill
            )

            matched_jd_keys.add(
                jd_key
            )

            details.append(
                {
                    "resume_skill": resume_skill,
                    "jd_skill": jd_skill,
                    "match_type": "exact",
                    "confidence": 1.0,
                    "reason": "Exact normalized skill match.",
                }
            )

    # ---------------------------------------------------------------
    # Deterministic equivalent matching
    # ---------------------------------------------------------------

    for jd_key, jd_skill in jd_lookup.items():
        if jd_key in matched_jd_keys:
            continue

        for resume_skill in resume_skills:
            if _is_deterministically_related(
                resume_skill,
                jd_skill,
            ):
                related_matches.append(
                    jd_skill
                )

                matched_jd_keys.add(
                    jd_key
                )

                details.append(
                    {
                        "resume_skill": resume_skill,
                        "jd_skill": jd_skill,
                        "match_type": "related",
                        "confidence": 0.95,
                        "reason": (
                            "Strong equivalent or naming relationship."
                        ),
                    }
                )

                break

    # ---------------------------------------------------------------
    # AI semantic matching
    # ---------------------------------------------------------------

    for item in ai_matches:
        jd_skill = item.get(
            "jd_skill"
        )

        resume_skill = item.get(
            "resume_skill"
        )

        if not isinstance(
            jd_skill,
            str,
        ):
            continue

        if not isinstance(
            resume_skill,
            str,
        ):
            continue

        jd_key = _normalize(
            jd_skill
        )

        if jd_key not in jd_lookup:
            continue

        if jd_key in matched_jd_keys:
            continue

        match_type = item.get(
            "match_type"
        )

        if match_type == "related":
            related_matches.append(
                jd_lookup[jd_key]
            )

            matched_jd_keys.add(
                jd_key
            )

            details.append(
                item
            )

        elif match_type == "partial":
            partial_matches.append(
                jd_lookup[jd_key]
            )

            matched_jd_keys.add(
                jd_key
            )

            details.append(
                item
            )

    # ---------------------------------------------------------------
    # Missing skills
    # ---------------------------------------------------------------

    for jd_key, jd_skill in jd_lookup.items():
        if jd_key in matched_jd_keys:
            continue

        missing_matches.append(
            jd_skill
        )

        details.append(
            {
                "resume_skill": "",
                "jd_skill": jd_skill,
                "match_type": "missing",
                "confidence": 1.0,
                "reason": (
                    "No sufficiently strong resume evidence."
                ),
            }
        )

    # ---------------------------------------------------------------
    # Score
    # ---------------------------------------------------------------

    total = len(jd_skills)

    if total == 0:
        score = 100.0

    else:
        weighted_points = (
            len(exact_matches) * 1.0
            + len(related_matches) * 0.5
            + len(partial_matches) * 0.25
        )

        score = round(
            weighted_points
            / total
            * 100,
            2,
        )

    return {
        "matched": (
            exact_matches
            + related_matches
            + partial_matches
        ),
        "exact_matches": exact_matches,
        "related_matches": related_matches,
        "partial_matches": partial_matches,
        "missing": missing_matches,
        "score": score,
        "match_details": details,
    }


# ---------------------------------------------------------------------------
# Public matcher
# ---------------------------------------------------------------------------

def match_skills(
    resume_skills: list[str],
    required_skills: list[str],
    preferred_skills: list[str] | None = None,
    *,
    provider: Any = None,
) -> dict[str, Any]:
    """
    Match resume skills against required and preferred JD skills.
    """

    resume_skills = _dedupe(
        resume_skills
    )

    required_skills = _dedupe(
        required_skills
    )

    preferred_skills = _dedupe(
        preferred_skills or []
    )

    # ---------------------------------------------------------------
    # One AI call for all semantic matching.
    # ---------------------------------------------------------------

    ai_required: list[dict[str, Any]] = []
    ai_preferred: list[dict[str, Any]] = []

    if provider is not None and (
        required_skills
        or preferred_skills
    ):
        ai_result = _ai_semantic_match(
            provider,
            resume_skills,
            required_skills,
            preferred_skills,
        )

        validated = _validate_ai_matches(
            ai_result,
            resume_skills,
            required_skills,
            preferred_skills,
        )

        required_keys = {
            _normalize(skill)
            for skill in required_skills
        }

        preferred_keys = {
            _normalize(skill)
            for skill in preferred_skills
        }

        for item in validated:
            jd_key = _normalize(
                item["jd_skill"]
            )

            if jd_key in required_keys:
                ai_required.append(
                    item
                )

            elif jd_key in preferred_keys:
                ai_preferred.append(
                    item
                )

    # ---------------------------------------------------------------
    # Match required and preferred categories.
    # ---------------------------------------------------------------

    required_result = _match_category(
        resume_skills,
        required_skills,
        ai_required,
    )

    preferred_result = _match_category(
        resume_skills,
        preferred_skills,
        ai_preferred,
    )

    # ---------------------------------------------------------------
    # Combined matched skills.
    # ---------------------------------------------------------------

    matched_skills = _dedupe(
        required_result["matched"]
        + preferred_result["matched"]
    )

    # ---------------------------------------------------------------
    # Extra resume skills.
    # ---------------------------------------------------------------

    jd_skill_keys = {
        _normalize(skill)
        for skill in (
            required_skills
            + preferred_skills
        )
    }

    extra_resume_skills = [
        skill
        for skill in resume_skills
        if _normalize(skill)
        not in jd_skill_keys
    ]

    # ---------------------------------------------------------------
    # Overall score.
    # ---------------------------------------------------------------

    if (
        required_skills
        and preferred_skills
    ):
        skill_match_percentage = round(
            required_result["score"] * 0.80
            + preferred_result["score"] * 0.20,
            2,
        )

    elif required_skills:
        skill_match_percentage = (
            required_result["score"]
        )

    elif preferred_skills:
        skill_match_percentage = (
            preferred_result["score"]
        )

    else:
        skill_match_percentage = 100.0

    # ---------------------------------------------------------------
    # Final result.
    # ---------------------------------------------------------------

    return {
        "matched_skills": matched_skills,

        "matched_required": (
            required_result["matched"]
        ),

        "matched_preferred": (
            preferred_result["matched"]
        ),

        "exact_matches": (
            required_result["exact_matches"]
            + preferred_result["exact_matches"]
        ),

        "related_matches": (
            required_result["related_matches"]
            + preferred_result["related_matches"]
        ),

        "partial_matches": (
            required_result["partial_matches"]
            + preferred_result["partial_matches"]
        ),

        "missing_required": (
            required_result["missing"]
        ),

        "missing_preferred": (
            preferred_result["missing"]
        ),

        "extra_resume_skills": (
            extra_resume_skills
        ),

        "required_skill_score": (
            required_result["score"]
        ),

        "preferred_skill_score": (
            preferred_result["score"]
        ),

        "skill_match_percentage": (
            skill_match_percentage
        ),

        "required_match_details": (
            required_result["match_details"]
        ),

        "preferred_match_details": (
            preferred_result["match_details"]
        ),
    }


# ---------------------------------------------------------------------------
# Backward-compatible helper
# ---------------------------------------------------------------------------

def match_skill_lists(
    resume_skills: list[str],
    jd_skills: list[str],
    provider: Any = None,
) -> dict[str, Any]:
    """Backward-compatible single-list matcher."""

    return match_skills(
        resume_skills,
        jd_skills,
        [],
        provider=provider,
    )
