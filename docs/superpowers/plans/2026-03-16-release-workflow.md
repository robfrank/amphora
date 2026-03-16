# Release Workflow Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement a manual release workflow with automated versioning, changelog generation, GPG signing, and Maven Central publishing.

**Architecture:** A GitHub Actions workflow triggered by `workflow_dispatch` that uses `release-it` for versioning/changelog and Maven for building/signing/publishing.

**Tech Stack:** Java 25, Maven, GitHub Actions, release-it, GPG.

---

## Chunk 1: Maven Configuration for Release

**Files:**
- Modify: `pom.xml`

### Task 1: Add release-related plugins and profile to `pom.xml`

- [ ] **Step 1: Update `pom.xml` with source, javadoc, and gpg plugins**

```xml
<project>
    <!-- ... (existing properties) ... -->
    <properties>
        <!-- ... (existing properties) ... -->
        <maven.source.plugin.version>3.3.1</maven.source.plugin.version>
        <maven.javadoc.plugin.version>3.11.1</maven.javadoc.plugin.version>
        <maven.gpg.plugin.version>3.2.7</maven.gpg.plugin.version>
        <central-publishing-maven-plugin.version>0.6.0</central-publishing-maven-plugin.version>
    </properties>

    <build>
        <plugins>
            <!-- ... (existing plugins) ... -->
            <plugin>
                <groupId>org.sonatype.central</groupId>
                <artifactId>central-publishing-maven-plugin</artifactId>
                <version>${central-publishing-maven-plugin.version}</version>
                <extensions>true</extensions>
                <configuration>
                    <publishingServerId>central</publishingServerId>
                </configuration>
            </plugin>
        </plugins>
    </build>

    <profiles>
        <profile>
            <id>release</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-source-plugin</artifactId>
                        <version>${maven.source.plugin.version}</version>
                        <executions>
                            <execution>
                                <id>attach-sources</id>
                                <goals>
                                    <goal>jar-no-fork</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-javadoc-plugin</artifactId>
                        <version>${maven.javadoc.plugin.version}</version>
                        <executions>
                            <execution>
                                <id>attach-javadocs</id>
                                <goals>
                                    <goal>jar</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-gpg-plugin</artifactId>
                        <version>${maven.gpg.plugin.version}</version>
                        <executions>
                            <execution>
                                <id>sign-artifacts</id>
                                <phase>verify</phase>
                                <goals>
                                    <goal>sign</goal>
                                </goals>
                                <configuration>
                                    <gpgArguments>
                                        <arg>--pinentry-mode</arg>
                                        <arg>loopback</arg>
                                    </gpgArguments>
                                </configuration>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>

    <distributionManagement>
        <snapshotRepository>
            <id>ossrh</id>
            <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
        </snapshotRepository>
    </distributionManagement>
</project>
```

- [ ] **Step 2: Verify Maven configuration**

Run: `mvn help:effective-pom -Prelease`
Expected: SUCCESS, verify javadoc, source, and gpg plugins are present in the output.

- [ ] **Step 3: Commit `pom.xml` changes**

```bash
git add pom.xml
git commit -m "build: add release profile and plugins for Maven Central"
```

---

## Chunk 2: Release-it Configuration

**Files:**
- Create: `.release-it.json`

### Task 2: Configure `release-it` for Amphora

- [ ] **Step 1: Create `.release-it.json`**

```json
{
  "git": {
    "commitMessage": "chore: release v${version}",
    "tagName": "v${version}",
    "requireCleanWorkingDir": false
  },
  "github": {
    "release": true,
    "releaseName": "v${version}"
  },
  "plugins": {
    "@release-it/conventional-changelog": {
      "infile": "CHANGELOG.md",
      "preset": "conventionalcommits"
    }
  },
  "hooks": {
    "after:bump": "mvn versions:set -DnewVersion=${version} -DgenerateBackupPoms=false"
  }
}
```

- [ ] **Step 2: Commit `.release-it.json`**

```bash
git add .release-it.json
git commit -m "build: add release-it configuration"
```

---

## Chunk 3: GitHub Actions Workflow

**Files:**
- Create: `.github/workflows/release.yml`

### Task 3: Implement the release workflow

- [ ] **Step 1: Create `.github/workflows/release.yml`**

```yaml
name: Release

on:
  workflow_dispatch:
    inputs:
      version:
        description: 'Version to release (e.g. 1.2.0)'
        required: true
      dry_run:
        description: 'Dry run (simulate the release)'
        type: boolean
        default: false

jobs:
  release:
    runs-on: ubuntu-latest
    permissions:
      contents: write
      packages: write

    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0
          token: ${{ secrets.GITHUB_TOKEN }}

      - name: Set up JDK 25
        uses: actions/setup-java@v4
        with:
          java-version: '25'
          distribution: 'temurin'
          cache: maven
          server-id: central
          server-username: MAVEN_CENTRAL_USERNAME
          server-password: MAVEN_CENTRAL_PASSWORD
          gpg-private-key: ${{ secrets.GPG_PRIVATE_KEY }}
          gpg-passphrase: GPG_PASSPHRASE

      - name: Setup Node.js
        uses: actions/setup-node@v4
        with:
          node-version: 'lts/*'

      - name: Install release-it
        run: npm install -g release-it @release-it/conventional-changelog

      - name: Config Git
        run: |
          git config user.name "github-actions[bot]"
          git config user.email "github-actions[bot]@users.noreply.github.com"

      - name: Run release-it
        run: |
          release-it ${{ github.event.inputs.version }} --ci ${{ github.event.inputs.dry_run && '--dry-run' || '' }}
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}

      - name: Build and Publish to Maven Central
        if: ${{ !github.event.inputs.dry_run }}
        run: mvn clean deploy -Prelease -DskipTests
        env:
          MAVEN_CENTRAL_USERNAME: ${{ secrets.MAVEN_CENTRAL_USERNAME }}
          MAVEN_CENTRAL_PASSWORD: ${{ secrets.MAVEN_CENTRAL_PASSWORD }}
          GPG_PASSPHRASE: ${{ secrets.GPG_PASSPHRASE }}
```

- [ ] **Step 2: Commit `.github/workflows/release.yml`**

```bash
git add .github/workflows/release.yml
git commit -m "ci: add release workflow"
```

---

## Chunk 4: Documentation

**Files:**
- Modify: `README.md`

### Task 4: Update README with release instructions

- [ ] **Step 1: Add a "Releasing" section to `README.md`**

```markdown
## Releasing

To release a new version of Amphora:

1. Go to the **Actions** tab in GitHub.
2. Select the **Release** workflow.
3. Click **Run workflow**.
4. Enter the new version (e.g., `1.0.0`) and choose whether to do a dry run.
5. Click **Run workflow**.

The workflow will:
- Update the version in `pom.xml`.
- Generate `CHANGELOG.md` based on Conventional Commits.
- Create a Git tag and GitHub Release.
- Sign and publish artifacts to Maven Central.
```

- [ ] **Step 2: Commit `README.md` changes**

```bash
git add README.md
git commit -m "docs: add release instructions to README"
```
