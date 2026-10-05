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

    <title>Đăng nhập</title>
  </head>

  <body class="bg-light d-flex flex-column min-vh-100">
    <%@ include file="fragments/user-header.jspf" %>

    <main class="container py-4 flex-grow-1">
      <div class="row justify-content-center">
        <div class="col-md-7 col-lg-5">
          <div class="card shadow-sm">
            <div class="card-header">
              <h1 class="h4 mb-0">
                Đăng nhập
              </h1>
            </div>

            <div class="card-body">
              <c:if test="${not empty successMessage}">
                <div class="alert alert-success">
                  <c:out value="${successMessage}" />
                </div>
              </c:if>

              <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">
                  <c:out value="${errorMessage}" />
                </div>
              </c:if>

              <form
                method="post"
                action="${pageContext.request.contextPath}/login"
              >
                <div class="mb-3">
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
                    maxlength="50"
                    required
                    autofocus
                  />
                </div>

                <div class="mb-3">
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
                    required
                  />
                </div>

                <button
                  type="submit"
                  class="btn btn-primary w-100"
                >
                  Đăng nhập
                </button>
              </form>

              <hr />

              <div class="text-center">
                Chưa có tài khoản?

                <a
                  href="${pageContext.request.contextPath}/register"
                >
                  Đăng ký
                </a>
              </div>

              <div class="alert alert-secondary mt-3 mb-0">
                <strong>Tài khoản Admin mẫu:</strong><br />
                Username: admin<br />
                Password: admin123
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <%@ include file="fragments/footer.jspf" %>
  </body>
</html>
