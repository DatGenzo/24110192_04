<!doctype html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />

    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <title><sitemesh:write property="title" /></title>

    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />

    <link href="/24110192_04/assets/css/app.css" rel="stylesheet" />

    <sitemesh:write property="head" />
  </head>

  <body class="bg-light d-flex flex-column min-vh-100">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
      <div class="container">
        <a class="navbar-brand" href="/24110192_04/admin"> Video Admin </a>

        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#adminNavigation"
          aria-controls="adminNavigation"
          aria-expanded="false"
          aria-label="Mở menu"
        >
          <span class="navbar-toggler-icon"></span>
        </button>

        <div id="adminNavigation" class="collapse navbar-collapse">
          <div class="navbar-nav ms-auto">
            <a class="nav-link" href="/24110192_04/home"> Trang chủ </a>

            <a class="nav-link" href="/24110192_04/videos"> Sản phẩm </a>

            <a class="nav-link" href="/24110192_04/admin/users">
              Quản lý User
            </a>

            <a class="nav-link" href="/24110192_04/admin"> Trang quản trị </a>

            <a class="nav-link" href="/24110192_04/logout"> Đăng xuất </a>
          </div>
        </div>
      </div>
    </nav>

    <main class="container py-4 flex-grow-1">
      <sitemesh:write property="body" />
    </main>

    <footer class="bg-primary text-white text-center py-3">
      <div>
        &#272;&#7895; Th&#224;nh &#272;&#7841;t | MSSV: 24110192 | M&#227;
        &#273;&#7873;: 04
      </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html>
