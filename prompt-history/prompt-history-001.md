agravell047: model: gpt-4.1
mode: update

task: |
You are a senior Java Spring Boot engineer. Review the full codebase and enforce all structural, naming, and architectural conventions described in the copilot-instructions.md file located at the root of this project.

Your tasks:

- Analyze Java classes, configuration files, and package structure for consistency with copilot-instructions.md.
- Refactor or relocate code that does not follow the documented package organization, naming patterns, or usage conventions.
- Remove or rewrite anti-patterns, redundant code, or inconsistencies that violate the project’s defined style.
- Do not modify existing business logic—only improve structure, naming, and adherence to conventions.
- Ensure the application builds and runs successfully using both `mvn clean install` and `mvn spring-boot:run`. If `mvn clean install` fails, your changes are not considered complete.

Action:

- Apply all necessary changes directly to the codebase to bring it into compliance with copilot-instructions.md.
- Do not return suggestions—make the changes directly and confirm the application builds.

role: software engineer
persona: senior Java Spring Boot engineer

output: |

- Codebase fully compliant with copilot-instructions.md
- Structure, naming, and conventions enforced
- Application builds and runs successfully using mvn clean install

constraints:

- Do not change business logic
- Only update for structure, naming, and conventions
- Ensure application builds and runs

context:

- Java Spring Boot project
- Codebase structure and convention enforcement

notes: |

- Focus on structure, naming, and best practices only.
- mvn clean install must pass for work to be accepted.

GitHub Copilot:

```

```

Made changes.

```

```

Made changes.

Continue to iterate?
Copilot has been working on this problem for a while. It can continue to iterate, or you can send a new message to refine your prompt.

agravell047: @agent Continue: "Continue to iterate?"

GitHub Copilot:

```

```

Made changes.

```

```

Made changes.

```

```

Made changes.

```

```

Made changes.

```

```

Made changes.

- Codebase fully compliant with copilot-instructions.md
- Structure, naming, and conventions enforced (package declarations, imports, dependencies, controller and test structure)
- Application builds and tests run successfully using `mvn clean install`

No business logic was changed. The project is now structurally and conventionally correct and ready for further development.
