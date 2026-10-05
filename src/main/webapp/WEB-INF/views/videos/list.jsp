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
    <title>Danh sách Video</title>
  </head>
  <body class="bg-light d-flex flex-column min-vh-100">
    <%@ include file="../fragments/user-header.jspf" %>
    <main class="container py-4 flex-grow-1">

<div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-4">
  <div>
    <h1 class="h2 mb-1">Danh sách Video</h1>

    <p class="text-muted mb-0">
      Video được phân nhóm theo Category và phân trang 3 Video/trang
    </p>
  </div>

  <span class="badge bg-primary fs-6">
    <c:out value="${catalogPage.totalItems}" />
    Video
  </span>
</div>

<c:if test="${not empty errorMessage}">
  <div class="alert alert-danger">
    <c:out value="${errorMessage}" />
  </div>
</c:if>

<div class="card shadow-sm mb-4">
  <div class="card-header">
    <strong>Category và số lượng Video</strong>
  </div>

  <div class="card-body">
    <div class="d-flex flex-wrap gap-2">
      <a
        href="${pageContext.request.contextPath}/videos"
        class="btn ${empty catalogPage.selectedCategoryId
          ? 'btn-primary'
          : 'btn-outline-primary'}"
      >
        Tất cả
      </a>

      <c:forEach
        items="${catalogPage.categories}"
        var="category"
      >
        <c:url
          var="categoryUrl"
          value="/videos"
        >
          <c:param
            name="categoryId"
            value="${category.categoryId}"
          />
          <c:param
            name="page"
            value="1"
          />
        </c:url>

        <a
          href="${categoryUrl}"
          class="btn ${catalogPage.selectedCategoryId
            == category.categoryId
              ? 'btn-primary'
              : 'btn-outline-primary'}"
        >
          <c:out value="${category.categoryName}" />

          <span
            class="badge ${catalogPage.selectedCategoryId
              == category.categoryId
                ? 'bg-light text-primary'
                : 'bg-primary'} ms-1"
          >
            <c:out value="${category.videoCount}" />
          </span>
        </a>
      </c:forEach>
    </div>
  </div>
</div>

<c:choose>
  <c:when test="${empty catalogPage.groups}">
    <div class="alert alert-info">
      Category đang chọn chưa có Video hoạt động.
    </div>
  </c:when>

  <c:otherwise>
    <c:forEach
      items="${catalogPage.groups}"
      var="group"
    >
      <section class="mb-5">
        <div class="d-flex justify-content-between align-items-center border-bottom pb-2 mb-3">
          <h2 class="h4 mb-0">
            <c:out value="${group.category.categoryName}" />
          </h2>

          <span class="badge bg-secondary">
            Tổng:
            <c:out value="${group.category.videoCount}" />
            Video
          </span>
        </div>

        <div class="row g-4">
          <c:forEach
            items="${group.videos}"
            var="video"
          >
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

            <c:url
              var="detailUrl"
              value="/videos/detail"
            >
              <c:param
                name="id"
                value="${video.videoId}"
              />
            </c:url>

            <div class="col-md-6 col-xl-4">
              <article class="card h-100 shadow-sm">
                <a href="${detailUrl}">
                  <img
                    src="<c:out value='${posterUrl}' />"
                    alt="<c:out value='${video.title}' />"
                    class="card-img-top"
                    style="height: 220px; object-fit: cover"
                    onerror="this.onerror=null;this.src='${placeholderUrl}';"
                  />
                </a>

                <div class="card-body d-flex flex-column">
                  <div class="mb-2">
                    <span class="badge bg-primary">
                      <c:out value="${video.categoryName}" />
                    </span>

                    <span class="badge bg-light text-dark border">
                      <c:out value="${video.videoId}" />
                    </span>
                  </div>

                  <h3 class="h5 card-title">
                    <c:out value="${video.title}" />
                  </h3>

                  <div class="text-muted small mb-3">
                    <c:choose>
                      <c:when test="${empty video.description}">
                        Chưa có mô tả.
                      </c:when>

                      <c:when test="${fn:length(video.description) > 120}">
                        <c:out
                          value="${fn:substring(video.description, 0, 120)}"
                        />...
                      </c:when>

                      <c:otherwise>
                        <c:out value="${video.description}" />
                      </c:otherwise>
                    </c:choose>
                  </div>

                  <div class="d-flex justify-content-between align-items-center mb-3">
                    <strong class="fs-5 text-danger">
                      <fmt:formatNumber
                        value="${video.unitPrice}"
                        type="number"
                        groupingUsed="true"
                      /> đ
                    </strong>

                    <span class="badge ${video.stockQuantity > 0 ? 'bg-success' : 'bg-danger'}">
                      <c:choose>
                        <c:when test="${video.stockQuantity > 0}">
                          Còn <c:out value="${video.stockQuantity}" />
                        </c:when>
                        <c:otherwise>Hết hàng</c:otherwise>
                      </c:choose>
                    </span>
                  </div>

                  <div class="d-flex justify-content-between small text-muted border-top pt-3 mt-auto">
                    <span>
                      👁
                      <c:out value="${video.views}" />
                    </span>

                    <span>
                      ♥
                      <c:out value="${video.favoriteCount}" />
                    </span>

                    <span>
                      ↗
                      <c:out value="${video.shareCount}" />
                    </span>
                  </div>

                  <a
                    href="${detailUrl}"
                    class="btn btn-outline-primary mt-3"
                  >
                    Xem chi tiết
                  </a>

                  <c:if test="${not empty sessionScope.currentUser and not sessionScope.currentUser.admin}">
                    <form
                      method="post"
                      action="${pageContext.request.contextPath}/cart/add"
                      class="d-flex gap-2 mt-2"
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
                        Thêm
                      </button>
                    </form>
                  </c:if>
                </div>
              </article>
            </div>
          </c:forEach>
        </div>
      </section>
    </c:forEach>
  </c:otherwise>
</c:choose>

<c:if test="${catalogPage.totalPages > 1}">
  <nav aria-label="Phân trang Video">
    <ul class="pagination justify-content-center">
      <c:url var="previousUrl" value="/videos">
        <c:param
          name="page"
          value="${catalogPage.page - 1}"
        />

        <c:param
          name="categoryId"
          value="${catalogPage.selectedCategoryId}"
        />
      </c:url>

      <li class="page-item ${catalogPage.first ? 'disabled' : ''}">
        <a
          class="page-link"
          href="${previousUrl}"
        >
          Trước
        </a>
      </li>

      <c:forEach
        begin="1"
        end="${catalogPage.totalPages}"
        var="pageNumber"
      >
        <c:url var="pageUrl" value="/videos">
          <c:param
            name="page"
            value="${pageNumber}"
          />

          <c:param
            name="categoryId"
            value="${catalogPage.selectedCategoryId}"
          />
        </c:url>

        <li
          class="page-item ${pageNumber == catalogPage.page
            ? 'active'
            : ''}"
        >
          <a
            class="page-link"
            href="${pageUrl}"
          >
            <c:out value="${pageNumber}" />
          </a>
        </li>
      </c:forEach>

      <c:url var="nextUrl" value="/videos">
        <c:param
          name="page"
          value="${catalogPage.page + 1}"
        />

        <c:param
          name="categoryId"
          value="${catalogPage.selectedCategoryId}"
        />
      </c:url>

      <li class="page-item ${catalogPage.last ? 'disabled' : ''}">
        <a
          class="page-link"
          href="${nextUrl}"
        >
          Sau
        </a>
      </li>
    </ul>
  </nav>
</c:if>

    </main>
    <%@ include file="../fragments/footer.jspf" %>
  </body>
</html>
