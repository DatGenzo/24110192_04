<%@ page
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>

<!doctype html>
<html lang="vi">
  <head>
    <%@ include file="../fragments/head.jspf" %>

    <title>Trang quản trị</title>
  </head>

  <body class="bg-light d-flex flex-column min-vh-100">
    <%@ include file="../fragments/admin-header.jspf" %>

    <main class="container py-4 flex-grow-1">
      <div
        class="d-flex justify-content-between align-items-center mb-4"
      >
        <div>
          <h1 class="h2 mb-1">
            Trang quản trị
          </h1>

          <p class="text-muted mb-0">
            Quản lý hệ thống Video
          </p>
        </div>

        <span class="badge text-bg-primary fs-6">
          ADMIN
        </span>
      </div>

      <div class="row g-4">
        <div class="col-md-4">
          <div class="card stat-card h-100 shadow-sm">
            <div class="card-body text-center">
              <div class="text-muted">Category</div>

              <div class="stat-value text-primary">
                ${stats.categoryCount}
              </div>
            </div>
          </div>
        </div>

        <div class="col-md-4">
          <div class="card stat-card h-100 shadow-sm">
            <div class="card-body text-center">
              <div class="text-muted">Video</div>

              <div class="stat-value text-success">
                ${stats.videoCount}
              </div>
            </div>
          </div>
        </div>

        <div class="col-md-4">
          <div class="card stat-card h-100 shadow-sm">
            <div class="card-body text-center">
              <div class="text-muted">User</div>

              <div class="stat-value text-warning">
                ${stats.userCount}
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <%@ include file="../fragments/footer.jspf" %>
  </body>
</html>
