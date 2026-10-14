# Nhat ky su dung AI (Y6)

Luu y: file nay la khung. Cac muc "Chinh sua cua toi" va "Nhan xet" ban phai tu viet,
vi de bai yeu cau minh chung la prompt that va quyet dinh cua chinh ban.

## 1. Thiet ke Test Case & Test Plan (Gen AI + RAG)
- Prompt: (dan prompt ban dung, kem phan business rule lam ngu canh RAG, xem docs/TestPlan.md muc 2)
- Ban AI sinh: (dan nguyen van)
- Chinh sua cua toi: (liet ke them / bo / sua gi va ly do)

## 2. Page Object & Test Script (Cursor AI)
- Prompt:
- Code AI sinh vs code cuoi (src/test/java/com/example/shop/ui):
- Chinh sua cua toi:

## 3. Xu ly loi & Debug
- Loi that da gap: `SessionNotCreatedException: cannot find Chrome binary` khi chay `mvn test -Pui`.
- Stack trace da dua cho AI:
- Cach AI ho tro / cach toi xu ly:

## 4. GitHub Actions
- Workflow: .github/workflows/selenium-ci.yml
- Giai thich: `on` (push, pull_request, workflow_dispatch), job `unit-tests` chay truoc,
  job `ui-tests` dung matrix theo group (smoke mac dinh, chon smoke/regression/all khi chay tay),
  `actions/upload-artifact` luu `target/surefire-reports/`.
- Trang thai: chua push len GitHub nen chua chay thu.

## 5. Toi uu & Review
- De xuat cua AI:
- Quyet dinh cua toi:
