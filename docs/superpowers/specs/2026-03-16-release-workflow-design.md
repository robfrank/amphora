# Release Workflow with Automated Release Notes Design

This document outlines the design for a manual release workflow for the Amphora project, including automated versioning, `CHANGELOG.md` generation, GPG signing, and publishing to Maven Central.

## Goals
- Provide a manual trigger via GitHub Actions for controlled releases.
- Automate the update of `pom.xml` version and `CHANGELOG.md` based on Conventional Commits.
- Securely sign artifacts with GPG.
- Publish artifacts (JAR, Sources, Javadoc) to Maven Central (OSSRH S01).
- Create a GitHub Release with the corresponding changelog entry.

## Architecture

### Workflow Flow (`release.yml`)
1.  **Manual Trigger (`workflow_dispatch`):**
    - Input: `version` (e.g., `1.2.0`).
    - Input: `is_snapshot` (boolean, default: `false`).
2.  **Setup:**
    - Checkout code with full history (required for changelog generation).
    - Setup JDK 25.
    - Setup GPG (import private key from secrets).
3.  **Versioning & Changelog:**
    - Use `release-it` (or a similar tool) to:
        - Update `<version>` in `pom.xml`.
        - Append new Conventional Commit entries to `CHANGELOG.md`.
        - Commit changes and create a Git tag (e.g., `v1.2.0`).
        - Push the tag and commit back to `master`.
4.  **Build & Sign:**
    - Run `mvn clean deploy -Prelease` with GPG signing enabled.
    - Maven will package the main JAR, sources JAR, and javadoc JAR.
    - Artifacts are signed with the provided GPG key.
5.  **Publish to Maven Central:**
    - Use `central-publishing-maven-plugin` to deploy to the Central Portal.
6.  **GitHub Release:**
    - Extract the latest entry from `CHANGELOG.md`.
    - Create a GitHub Release using the tag and extracted changelog.
    - Attach the built artifacts to the release.

## Components

### GitHub Actions
- `actions/checkout@v4`: For code access.
- `actions/setup-java@v4`: For JDK 25 and Maven caching.
- `crazy-max/ghaction-import-gpg@v6`: For GPG key management.
- `release-it/release-it@v17`: (Or similar) For versioning and changelog.
- `softprops/action-gh-release@v2`: For creating the GitHub Release.

### Maven Plugins (in `pom.xml`)
- `maven-gpg-plugin`: For artifact signing.
- `maven-source-plugin`: For source JAR generation.
- `maven-javadoc-plugin`: For javadoc JAR generation.
- `central-publishing-maven-plugin`: For publishing to the new Central Portal.

### Secrets (Required in GitHub)
- `GPG_PRIVATE_KEY`: Your ASCII-armored GPG private key.
- `GPG_PASSPHRASE`: The passphrase for your GPG key.
- `MAVEN_CENTRAL_USERNAME`: Your Central Portal username/token ID.
- `MAVEN_CENTRAL_PASSWORD`: Your Central Portal password/token secret.

## Data Flow
1.  **User Input** -> Workflow starts.
2.  **Git History** -> Conventional Commits analyzed -> `CHANGELOG.md` updated.
3.  **Source Code** -> Maven Build -> Signed Artifacts (JARs).
4.  **Signed Artifacts** -> Maven Central Portal.
5.  **Tag & Changelog** -> GitHub Release page.

## Error Handling
- **Build Failure:** If the Maven build or tests fail, the workflow stops before tagging or publishing.
- **Publishing Failure:** If deployment to Central fails, the Git tag may still exist; manual cleanup or a "retry" mechanism may be needed.
- **Credential Issues:** Clear error messages if GPG or Maven Central secrets are missing.

## Testing Strategy
- **Dry Run:** Add a `dry_run` input to the workflow to simulate the process without pushing to Git or Maven Central.
- **Validation:** Verify that `CHANGELOG.md` is correctly updated and formatted.
- **Artifact Check:** Verify that all required JARs (main, sources, javadoc) are produced and signed.
