<%@ page
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>

<%@ taglib
    prefix="c"
    uri="jakarta.tags.core"
%>

<!doctype html>
<html lang="vi">
  <head>
    <%@ include file="fragments/head.jspf" %>

    <title>Xác nhận OTP</title>
  </head>

  <body class="bg-light d-flex flex-column min-vh-100">
    <%@ include file="fragments/user-header.jspf" %>

    <main class="container py-4 flex-grow-1">
      <div class="row justify-content-center">
        <div class="col-md-7 col-lg-5">
          <div class="card shadow-sm">
            <div class="card-header">
              <h1 class="h4 mb-0">
                Xác nhận OTP
              </h1>
            </div>

            <div class="card-body">
              <c:if test="${param.sent == '1'}">
                <div class="alert alert-success">
                  OTP đã được gửi đến Gmail của bạn.
                </div>
              </c:if>

              <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">
                  <c:out value="${errorMessage}" />
                </div>
              </c:if>

              <p>
                Nhập mã gồm 6 chữ số đã gửi đến:
              </p>

              <p class="fw-bold">
                <c:out value="${maskedEmail}" />
              </p>

              <form
                method="post"
                action="${pageContext.request.contextPath}/verify-otp"
              >
                <div class="mb-3">
                  <label
                    for="otp"
                    class="form-label"
                  >
                    Mã OTP
                  </label>

                  <input
                    id="otp"
                    name="otp"
                    type="text"
                    inputmode="numeric"
                    pattern="[0-9]{6}"
                    maxlength="6"
                    class="form-control form-control-lg text-center"
                    autocomplete="one-time-code"
                    required
                    autofocus
                  />
                </div>

                <button
                  type="submit"
                  class="btn btn-success w-100"
                >
                  Kích hoạt tài khoản
                </button>
              </form>

              <div class="form-text mt-3">
                OTP hết hạn sau 5 phút và được nhập
                tối đa 5 lần.
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <%@ include file="fragments/footer.jspf" %>
  </body>
</html>
