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

    <title>Đăng ký</title>
  </head>

  <body class="bg-light d-flex flex-column min-vh-100">
    <%@ include file="fragments/user-header.jspf" %>

    <main class="container py-4 flex-grow-1">
      <div class="row justify-content-center">
        <div class="col-lg-7">
          <div class="card shadow-sm">
            <div class="card-header">
              <h1 class="h4 mb-0">
                Đăng ký tài khoản
              </h1>
            </div>

            <div class="card-body">
              <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">
                  <c:out value="${errorMessage}" />
                </div>
              </c:if>

              <form
                method="post"
                action="${pageContext.request.contextPath}/register"
              >
                <div class="row">
                  <div class="col-md-6 mb-3">
                    <label
                      for="username"
                      class="form-label"
                    >
                      Username
                    </label>

                    <input
                      id="username"
                      name="username"
                      type="text"
                      class="form-control"
                      value="<c:out value='${param.username}' />"
                      minlength="4"
                      maxlength="50"
                      required
                      autofocus
                    />
                  </div>

                  <div class="col-md-6 mb-3">
                    <label
                      for="fullName"
                      class="form-label"
                    >
                      Họ và tên
                    </label>

                    <input
                      id="fullName"
                      name="fullName"
                      type="text"
                      class="form-control"
                      value="<c:out value='${param.fullName}' />"
                      maxlength="50"
                      required
                    />
                  </div>
                </div>

                <div class="row">
                  <div class="col-md-6 mb-3">
                    <label
                      for="email"
                      class="form-label"
                    >
                      Gmail nhận OTP
                    </label>

                    <input
                      id="email"
                      name="email"
                      type="email"
                      class="form-control"
                      value="<c:out value='${param.email}' />"
                      maxlength="150"
                      required
                    />
                  </div>

                  <div class="col-md-6 mb-3">
                    <label
                      for="phone"
                      class="form-label"
                    >
                      Số điện thoại
                    </label>

                    <input
                      id="phone"
                      name="phone"
                      type="tel"
                      class="form-control"
                      value="<c:out value='${param.phone}' />"
                      pattern="[0-9]{9,15}"
                      maxlength="15"
                      required
                    />
                  </div>
                </div>

                <div class="row">
                  <div class="col-md-6 mb-3">
                    <label
                      for="password"
                      class="form-label"
                    >
                      Mật khẩu
                    </label>

                    <input
                      id="password"
                      name="password"
                      type="password"
                      class="form-control"
                      minlength="6"
                      required
                    />
                  </div>

                  <div class="col-md-6 mb-3">
                    <label
                      for="confirmPassword"
                      class="form-label"
                    >
                      Xác nhận mật khẩu
                    </label>

                    <input
                      id="confirmPassword"
                      name="confirmPassword"
                      type="password"
                      class="form-control"
                      minlength="6"
                      required
                    />
                  </div>
                </div>

                <button
                  type="submit"
                  class="btn btn-primary"
                >
                  Đăng ký và gửi OTP
                </button>

                <a
                  class="btn btn-secondary"
                  href="${pageContext.request.contextPath}/login"
                >
                  Quay lại đăng nhập
                </a>
              </form>
            </div>
          </div>
        </div>
      </div>
    </main>

    <%@ include file="fragments/footer.jspf" %>
  </body>
</html>
