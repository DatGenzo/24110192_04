<%@ page
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>
<%@ taglib
    prefix="c"
    uri="jakarta.tags.core"
%>
<%@ taglib
    prefix="fn"
    uri="jakarta.tags.functions"
%>
<%@ taglib
    prefix="fmt"
    uri="jakarta.tags.fmt"
%>

<!doctype html>
<html lang="vi">
  <head>
    <%@ include file="../fragments/head.jspf" %>
    <title>Chi tiết Video</title>
  </head>
  <body class="bg-light d-flex flex-column min-vh-100">
    <%@ include file="../fragments/user-header.jspf" %>
    <main class="container py-4 flex-grow-1">

<c:choose>
  <c:when test="${not empty errorMessage}">
    <div class="alert alert-danger shadow-sm">
      <h1 class="h4">Không thể hiển thị Video</h1>

      <p class="mb-0">
        <c:out value="${errorMessage}" />
      </p>
    </div>

    <a
      href="${pageContext.request.contextPath}/home"
      class="btn btn-secondary"
    >
      Quay về trang chủ
    </a>
  </c:when>

  <c:otherwise>
    <c:set
      var="placeholderUrl"
      value="${pageContext.request.contextPath}/assets/images/video-placeholder.svg"
    />

    <c:choose>
      <c:when test="${empty video.poster}">
        <c:set
          var="posterUrl"
          value="${placeholderUrl}"
        />
      </c:when>

      <c:when test="${fn:startsWith(video.poster, 'http')}">
        <c:set
          var="posterUrl"
          value="${video.poster}"
        />
      </c:when>

      <c:when test="${fn:startsWith(video.poster, '/')}">
        <c:set
          var="posterUrl"
          value="${pageContext.request.contextPath}${video.poster}"
        />
      </c:when>

      <c:otherwise>
        <c:set
          var="posterUrl"
          value="${pageContext.request.contextPath}/assets/images/${video.poster}"
        />
      </c:otherwise>
    </c:choose>

    <div class="mb-4">
      <a
        href="${pageContext.request.contextPath}/home"
        class="btn btn-outline-secondary btn-sm"
      >
        ← Quay lại
      </a>
    </div>

    <article class="card shadow-sm overflow-hidden">
      <div class="row g-0">
        <div class="col-lg-7 bg-dark">
          <img
            src="<c:out value='${posterUrl}' />"
            alt="<c:out value='${video.title}' />"
            class="w-100 h-100"
            style="min-height: 420px; object-fit: cover"
            onerror="this.onerror=null;this.src='${placeholderUrl}';"
          />
        </div>

        <div class="col-lg-5">
          <div class="card-body p-4 h-100">
            <span class="badge bg-primary mb-3">
              <c:out value="${video.categoryName}" />
            </span>

            <h1 class="h2 mb-3">
              <c:out value="${video.title}" />
            </h1>

            <div class="text-muted mb-4">
              Mã Video:
              <strong>
                <c:out value="${video.videoId}" />
              </strong>
            </div>

            <div class="d-flex justify-content-between align-items-center border rounded p-3 mb-4">
              <div>
                <div class="small text-muted">Giá bán</div>
                <strong class="fs-3 text-danger">
                  <fmt:formatNumber
                    value="${video.unitPrice}"
                    type="number"
                    groupingUsed="true"
                  /> đ
                </strong>
              </div>

              <span class="badge fs-6 ${video.stockQuantity > 0 ? 'bg-success' : 'bg-danger'}">
                <c:choose>
                  <c:when test="${video.stockQuantity > 0}">
                    Còn <c:out value="${video.stockQuantity}" /> sản phẩm
                  </c:when>
                  <c:otherwise>Hết hàng</c:otherwise>
                </c:choose>
              </span>
            </div>

            <div class="row g-3 mb-4">
              <div class="col-4">
                <div class="border rounded p-3 text-center h-100">
                  <div class="fs-4 fw-bold text-primary">
                    <c:out value="${video.views}" />
                  </div>

                  <div class="small text-muted">
                    Lượt xem
                  </div>
                </div>
              </div>

              <div class="col-4">
                <div class="border rounded p-3 text-center h-100">
                  <div class="fs-4 fw-bold text-danger">
                    <c:out value="${video.favoriteCount}" />
                  </div>

                  <div class="small text-muted">
                    Lượt thích
                  </div>
                </div>
              </div>

              <div class="col-4">
                <div class="border rounded p-3 text-center h-100">
                  <div class="fs-4 fw-bold text-success">
                    <c:out value="${video.shareCount}" />
                  </div>

                  <div class="small text-muted">
                    Chia sẻ
                  </div>
                </div>
              </div>
            </div>

            <h2 class="h5">Mô tả</h2>

            <p
              class="mb-0"
              style="white-space: pre-line"
            ><c:out value="${video.description}" /></p>

            <c:choose>
              <c:when test="${not empty sessionScope.currentUser and not sessionScope.currentUser.admin}">
                <form
                  method="post"
                  action="${pageContext.request.contextPath}/cart/add"
                  class="d-flex gap-2 mt-4"
                >
                  <input
                    type="hidden"
                    name="videoId"
                    value="<c:out value='${video.videoId}' />"
                  />
                  <input
                    type="number"
                    name="quantity"
                    value="1"
                    min="1"
                    max="${video.stockQuantity < 99 ? video.stockQuantity : 99}"
                    class="form-control"
                    aria-label="Số lượng"
                    ${video.stockQuantity <= 0 ? 'disabled' : ''}
                  />
                  <button
                    type="submit"
                    class="btn btn-success"
                    ${video.stockQuantity <= 0 ? 'disabled' : ''}
                  >
                    Thêm vào giỏ hàng
                  </button>
                </form>
              </c:when>

              <c:when test="${empty sessionScope.currentUser}">
                <a
                  href="${pageContext.request.contextPath}/login"
                  class="btn btn-primary mt-4"
                >
                  Đăng nhập để mua hàng
                </a>
              </c:when>
            </c:choose>
          </div>
        </div>
      </div>
    </article>
  </c:otherwise>
</c:choose>

    </main>
    <%@ include file="../fragments/footer.jspf" %>
  </body>
</html>
