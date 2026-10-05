<%@ page
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>

<!doctype html>
<html lang="vi">
  <head>
    <%@ include file="fragments/head.jspf" %>

    <title>Trang chủ</title>
  </head>

  <body class="bg-light d-flex flex-column min-vh-100">
    <%@ include file="fragments/user-header.jspf" %>

    <main class="container py-4 flex-grow-1">
      <section class="p-4 p-md-5 mb-4 bg-white rounded shadow-sm">
        <h1 class="display-6 fw-bold">
          Website Video - Đề số 04
        </h1>

        <p class="lead mb-0">
          Maven, Servlet, JDBC, JSP và JSP Fragments
        </p>
      </section>

      <div class="row g-4">
        <div class="col-md-4">
          <div class="card stat-card h-100 shadow-sm">
            <div class="card-body text-center">
              <div class="text-muted">
                Category đang hoạt động
              </div>

              <div class="stat-value text-primary">
                ${stats.categoryCount}
              </div>
            </div>
          </div>
        </div>

        <div class="col-md-4">
          <div class="card stat-card h-100 shadow-sm">
            <div class="card-body text-center">
              <div class="text-muted">
                Video đang hoạt động
              </div>

              <div class="stat-value text-success">
                ${stats.videoCount}
              </div>
            </div>
          </div>
        </div>

        <div class="col-md-4">
          <div class="card stat-card h-100 shadow-sm">
            <div class="card-body text-center">
              <div class="text-muted">
                Tổng số User
              </div>

              <div class="stat-value text-warning">
                ${stats.userCount}
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <%@ include file="fragments/footer.jspf" %>
  </body>
</html>
