# Skill Registry — languagedive-backend

<!-- Updated by SDD Init on 2026-07-18. Managed in engram with topic_key: skill-registry -->

## Contract

Delegator-use index. Subagents load exact `SKILL.md` paths before work. Do NOT inject generated summaries — pass paths so subagents load the full runtime contract.

## Skills (User-level)

| Skill | Trigger / Description | Scope | Path |
| --- | --- | --- | --- |
| `branch-pr` | Create Gentle AI pull requests with issue-first checks. | user | `/home/Bryan/.config/opencode/skills/branch-pr/SKILL.md` |
| `chained-pr` | Split oversized changes into chained PRs. | user | `/home/Bryan/.config/opencode/skills/chained-pr/SKILL.md` |
| `cognitive-doc-design` | Design docs that reduce cognitive load. | user | `/home/Bryan/.config/opencode/skills/cognitive-doc-design/SKILL.md` |
| `comment-writer` | Write warm, direct collaboration comments. | user | `/home/Bryan/.config/opencode/skills/comment-writer/SKILL.md` |
| `customize-opencode` | Edit opencode's own configuration. | user | built-in |
| `go-testing` | Go tests, coverage, teatest, golden files. | user | `/home/Bryan/.config/opencode/skills/go-testing/SKILL.md` |
| `issue-creation` | Create GitHub issues with issue-first checks. | user | `/home/Bryan/.config/opencode/skills/issue-creation/SKILL.md` |
| `judgment-day` | Blind dual review, fix, re-judge. | user | `/home/Bryan/.config/opencode/skills/judgment-day/SKILL.md` |
| `skill-creator` | Create LLM-first skills with frontmatter. | user | `/home/Bryan/.config/opencode/skills/skill-creator/SKILL.md` |
| `skill-improver` | Audit/upgrade existing LLM-first skills. | user | `/home/Bryan/.config/opencode/skills/skill-improver/SKILL.md` |
| `skill-registry` | Index available skills by trigger/path. | user | `/home/Bryan/.config/opencode/skills/skill-registry/SKILL.md` |
| `work-unit-commits` | Plan commits as reviewable work units. | user | `/home/Bryan/.config/opencode/skills/work-unit-commits/SKILL.md` |
| `imagegen` | Generate/edit raster images. | user | `/home/Bryan/.codex/skills/.system/imagegen/SKILL.md` |
| `openai-docs` | OpenAI products, APIs, model selection. | user | `/home/Bryan/.codex/skills/.system/openai-docs/SKILL.md` |
| `plugin-creator` | Scaffold Codex plugin directories. | user | `/home/Bryan/.codex/skills/.system/plugin-creator/SKILL.md` |
| `skill-installer` | Install Codex skills from list or repo. | user | `/home/Bryan/.codex/skills/.system/skill-installer/SKILL.md` |

## Skills (SDD — excluded from registry scan per rules)

`sdd-init`, `sdd-onboard`, `sdd-propose`, `sdd-spec`, `sdd-design`, `sdd-tasks`, `sdd-apply`, `sdd-verify`, `sdd-archive`, `_shared`, `customize-opencode`

## Convention Files

- `~/.config/opencode/AGENTS.md` — persona, engram protocol, skill loading rules
- No project-level `AGENTS.md`, `CLAUDE.md`, `.cursorrules`, or `GEMINI.md` found

## Loading Protocol

1. Match task context and target files against the Trigger / Description column.
2. Pass only the matching `Path` values to subagents.
3. Instruct subagent to read those exact `SKILL.md` files before work.
4. If no match, report `skill_resolution: none`.
