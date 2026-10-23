# Upgrade Plan: shop-asm (20261024003551)

- **Generated**: 2026-10-24 00:55:00
- **HEAD Branch**: master
- **HEAD Commit ID**: 91426b0d248560c4fcd6c5fc91a47d510aef7b02

## Available Tools

**JDKs**
- JDK 17: C:\Program Files\Java\jdk-17 (current project JDK, used by step 2)
- JDK 25: C:\Program Files\Java\jdk-25.0.4.1 (target JDK, used by step 3, 5, 6)

**Build Tools**
- Maven 3.9.9: C:\Users\Moderator\tools\apache-maven-3.9.9

## Guidelines

> Note: You can add any specific guidelines or constraints for the upgrade process here if needed, bullet points are preferred.

## Options

- Working branch: appmod/java-upgrade-20261024003551
- Run tests before and after the upgrade: true

## Upgrade Goals

- Java: 25

## Technology Stack

| Technology/Dependency | Current | Min Compatible | Why Incompatible |
| --------------------- | ------- | -------------- | ---------------- |
| Java | 17 | 25 | User requested target version |
| Spring Boot | 3.3.5 | 3.5.16 | Spring Boot 3.3.x uses ASM that does not support Java 25 class file format (version 69); upgraded to 3.5.16 for full Java 25 compatibility |
| Maven | 3.9.9 | 3.9.0 | Compatible with Java 25 |
| Byte Buddy | 1.15.0 | 1.18.16 | Byte Buddy 1.15.0 does not recognize Java 25 VM class file version; 1.18.16 is required for Mockito mock maker on Java 25 |
| Mockito | 5.14.0 | 5.14.0 | Requires Byte Buddy 1.18.16 and -XX:+EnableDynamicAgentLoading on Java 25 |
| JUnit Jupiter | 5.10.3 | 5.10.3 | Managed by Spring Boot, compatible with Java 25 |
| TestNG | 7.10.2 | 7.10.2 | Compatible with Java 25 |
| Selenium Java | 4.25.0 | 4.25.0 | Compatible with Java 25 |

## Derived Upgrades

- **Java 25 → Spring Boot 3.5.16**: Spring Boot 3.3.5's classpath scanning fails with `BeanDefinitionStore Incompatible class format` on Java 25 compiled classes due to older ASM parser. Upgrading to Spring Boot 3.5.16 provides full Java 25 support.
- **Java 25 → Byte Buddy 1.18.16**: Byte Buddy in Spring Boot 3.3/Mockito 5.14 throws `IllegalStateException: Failed to resolve the class file version of the current VM: This JVM's version string does not seem to be valid: 25`. Upgrading Byte Buddy to 1.18.16 supports Java 25.
- **Java 25 → Surefire JVM Flags**: Java 25 restricts dynamic agent loading by default; adding `-XX:+EnableDynamicAgentLoading` and `-Dnet.bytebuddy.experimental=true` to maven-surefire-plugin `argLine` ensures Mockito inline mock maker works properly.
- **Java 25 → CI/CD Workflow**: Update GitHub Actions workflow `.github/workflows/selenium-ci.yml` `java-version` from `17` to `25`.

## Impact Analysis

### Dependency Changes

| File | Dependency | Current | Action | Target | Reason |
|------|-----------|---------|--------|--------|--------|
| pom.xml | java.version | 17 | upgrade | 25 | User requested target version |
| pom.xml | spring-boot-starter-parent | 3.3.5 | upgrade | 3.5.16 | Java 25 class file compatibility and Spring Framework 6.2+ ASM support |
| pom.xml | net.bytebuddy:byte-buddy | (transitive 1.15.0) | add | 1.18.16 | Support Java 25 VM class file version |
| pom.xml | net.bytebuddy:byte-buddy-agent | (transitive 1.15.0) | add | 1.18.16 | Support Java 25 agent attachment |
| pom.xml | maven-surefire-plugin (argLine) | `-Xmx1024m -XX:MaxMetaspaceSize=512m` | upgrade | `-Dnet.bytebuddy.experimental=true -XX:+EnableDynamicAgentLoading -Xmx1024m -XX:MaxMetaspaceSize=512m` | Enable experimental Byte Buddy support and dynamic agent loading for Mockito on Java 25 |

### Source Code Changes

No source code changes required. All Java source files compile without modification on Java 25.

### Configuration Changes

No configuration changes required.

### CI/CD Changes

| File | Location | Current | Required Change |
|------|----------|---------|----------------|
| .github/workflows/selenium-ci.yml | line 28 | java-version: "17" | Change to: "25" |
| .github/workflows/selenium-ci.yml | line 53 | java-version: "17" | Change to: "25" |

### Risks & Warnings

- **Byte Buddy & Mockito on Java 25**: Java 25 restricts dynamic agent loading and changes the class file version to 69. Byte Buddy 1.15.0 fails to detect this VM version, throwing `IllegalStateException`.
  - **Mitigation**: Explicitly add `net.bytebuddy:byte-buddy:1.18.16` and `net.bytebuddy:byte-buddy-agent:1.18.16` to test dependencies, and configure `-Dnet.bytebuddy.experimental=true -XX:+EnableDynamicAgentLoading` in surefire `argLine`.
- **Spring Framework class format scanning**: Spring Boot 3.3.5's embedded ASM library does not recognize class format 69 (Java 25), leading to BeanDefinitionStore Incompatible class format errors during integration tests.
  - **Mitigation**: Upgraded to Spring Boot 3.5.16 which includes updated Spring Framework and ASM supporting Java 25 bytecode.

## Upgrade Steps

- Step 1: Setup Environment
  - **Rationale**: Ensure required JDK 17, JDK 25, and Maven 3.9.9 are configured and ready.
  - **Changes to Make**: None; verify tool accessibility.
  - **Verification**: Command: `java -version && mvn -version`, JDK: C:\Program Files\Java\jdk-25.0.4.1, Expected Result: JDK 25 and Maven 3.9.9 available.

- Step 2: Setup Baseline
  - **Rationale**: Execute baseline compilation and tests with current JDK 17 to establish pass rate benchmark.
  - **Changes to Make**: None.
  - **Verification**: Command: `mvn clean compile test-compile -q && mvn test -q`, JDK: C:\Program Files\Java\jdk-17, Expected Result: Compilation SUCCESS, 47/47 tests pass.

- Step 3: Upgrade to Java 25, Spring Boot 3.5.16, and Byte Buddy 1.18.16
  - **Rationale**: Apply all dependency and plugin changes needed for Java 25 compatibility.
  - **Changes to Make**: Reference Impact Analysis: Dependency Changes in `pom.xml`. Update `java.version` to 25, `spring-boot-starter-parent` to 3.5.16, add Byte Buddy 1.18.16 dependencies, and update surefire `argLine`.
  - **Verification**: Command: `mvn clean test-compile -q`, JDK: C:\Program Files\Java\jdk-25.0.4.1, Expected Result: Compilation SUCCESS.

- Step 4: Update CI/CD Workflows
  - **Rationale**: Update GitHub Actions workflow to run on Java 25.
  - **Changes to Make**: Reference Impact Analysis: CI/CD Changes in `.github/workflows/selenium-ci.yml`.
  - **Verification**: Command: syntax validation of workflow file, Expected Result: File updated.

- Step 5: CVE Validation & Fix
  - **Rationale**: Scan dependencies for any known vulnerabilities and verify security posture.
  - **Changes to Make**: Verify dependency security status, fix if any found.
  - **Verification**: Command: `mvn clean test-compile -q`, JDK: C:\Program Files\Java\jdk-25.0.4.1, Expected Result: Build compiles cleanly.

- Step 6: Final Validation
  - **Rationale**: Verify all upgrade goals met, clean rebuild, and achieve 100% test pass rate with JDK 25.
  - **Changes to Make**: None.
  - **Verification**: Command: `mvn clean test`, JDK: C:\Program Files\Java\jdk-25.0.4.1, Expected Result: 47/47 tests passed (100% pass rate).
