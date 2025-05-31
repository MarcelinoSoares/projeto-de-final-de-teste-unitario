# Security Policy

## Supported Versions

| Version | Supported          |
| ------- | ------------------ |
| 2019.2  | :white_check_mark: |
| < 2019  | :x:                |

## Reporting a Vulnerability

If you discover a security vulnerability within this project, please follow these steps:

1. **DO NOT** open a public issue
2. Email the security team at: security@cesar.school
3. Include detailed steps to reproduce the vulnerability
4. Wait for acknowledgment (usually within 48 hours)

## Security Best Practices

### 1. Input Validation
- All user inputs are validated
- CPF validation prevents injection attacks
- Date inputs are bounded to prevent overflow

### 2. Data Protection
- No sensitive data is logged
- CPF numbers are validated but not stored in logs
- Personal information is handled with care

### 3. Dependencies
- Regular dependency updates via Dependabot
- OWASP Dependency Check in CI/CD pipeline
- No known vulnerabilities in current dependencies

### 4. Code Quality
- Static analysis with SonarQube
- Mutation testing to ensure test quality
- Code review required for all changes

## Security Checklist

- [ ] Input validation on all public methods
- [ ] No hardcoded credentials
- [ ] Secure random number generation for CPF
- [ ] No SQL injection vulnerabilities (N/A - no database)
- [ ] No XSS vulnerabilities (N/A - no web interface)
- [ ] Dependencies up to date
- [ ] Security headers configured (N/A - library project)

## Automated Security Scanning

This project uses:
- **OWASP Dependency Check**: Scans for known vulnerabilities
- **SonarQube**: Static security analysis
- **GitHub Security Advisories**: Automated alerts

## Contact

For security concerns, contact:
- Security Team: security@cesar.school
- Project Maintainer: @cesar-school 