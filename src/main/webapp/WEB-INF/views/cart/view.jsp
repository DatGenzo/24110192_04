<%@ page
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!doctype html>
<html lang="vi">
  <head>
    <%@ include file="../fragments/head.jspf" %>
    <title>Giỏ hàng</title>
  </head>
  <body class="bg-light d-flex flex-column min-vh-100">
    <%@ include file="../fragments/user-header.jspf" %>
    <main class="container py-4 flex-grow-1">

<div class="d-flex justify-content-between align-items-center mb-4">
  <div>
    <h1 class="h2 mb-1">Giỏ hàng</h1>
    <p class="text-muted mb-0">
      Thêm, xóa và thay đổi số lượng trong giới hạn tồn kho
    </p>
  </div>

  <a
    href="${pageContext.request.contextPath}/videos"
    class="btn btn-outline-primary"
  >
    Tiếp tục mua hàng
  </a>
</div>

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

<c:choose>
  <c:when test="${empty cart or cart.empty}">
    <div class="card shadow-sm">
      <div class="card-body text-center py-5">
        <h2 class="h4">Giỏ hàng đang trống</h2>
        <p class="text-muted">
          Hãy chọn sản phẩm trong danh sách Video.
        </p>
        <a
          href="${pageContext.request.contextPath}/videos"
          class="btn btn-primary"
        >
          Xem sản phẩm
        </a>
      </div>
    </div>
  </c:when>

  <c:otherwise>
    <div class="card shadow-sm overflow-hidden">
      <div class="table-responsive">
        <table class="table align-middle mb-0">
          <thead class="table-dark">
            <tr>
              <th>Sản phẩm</th>
              <th class="text-end">Đơn giá</th>
              <th style="width: 210px">Số lượng</th>
              <th class="text-end">Thành tiền</th>
              <th class="text-end">Thao tác</th>
            </tr>
          </thead>

          <tbody>
            <c:forEach items="${cart.items}" var="item">
              <c:set
                var="placeholderUrl"
                value="${pageContext.request.contextPath}/assets/images/video-placeholder.svg"
              />

              <c:choose>
                <c:when test="${empty item.poster}">
                  <c:set var="posterUrl" value="${placeholderUrl}" />
                </c:when>
                <c:when test="${fn:startsWith(item.poster, 'http')}">
                  <c:set var="posterUrl" value="${item.poster}" />
                </c:when>
                <c:when test="${fn:startsWith(item.poster, '/')}">
                  <c:set
                    var="posterUrl"
                    value="${pageContext.request.contextPath}${item.poster}"
                  />
                </c:when>
                <c:otherwise>
                  <c:set
                    var="posterUrl"
                    value="${pageContext.request.contextPath}/assets/images/${item.poster}"
                  />
                </c:otherwise>
              </c:choose>

              <tr>
                <td>
                  <div class="d-flex align-items-center gap-3">
                    <img
                      src="<c:out value='${posterUrl}' />"
                      alt="<c:out value='${item.title}' />"
                      class="rounded border"
                      style="width: 88px; height: 64px; object-fit: cover"
                      onerror="this.onerror=null;this.src='${placeholderUrl}';"
                    />
                    <div>
                      <div class="fw-semibold">
                        <c:out value="${item.title}" />
                      </div>
                      <div class="small text-muted">
                        <c:out value="${item.videoId}" />
                        · Còn <c:out value="${item.stockQuantity}" />
                      </div>
                      <c:if test="${not item.available}">
                        <span class="badge bg-danger">Đã ngừng bán</span>
                      </c:if>
                    </div>
                  </div>
                </td>

                <td class="text-end">
                  <fmt:formatNumber
                    value="${item.unitPrice}"
                    type="number"
                    groupingUsed="true"
                  /> đ
                </td>

                <td>
                  <form
                    method="post"
                    action="${pageContext.request.contextPath}/cart/update"
                    class="d-flex gap-2"
                  >
                    <input
                      type="hidden"
                      name="videoId"
                      value="<c:out value='${item.videoId}' />"
                    />
                    <input
                      type="number"
                      name="quantity"
                      value="${item.quantity}"
                      min="1"
                      max="${item.stockQuantity < 99 ? item.stockQuantity : 99}"
                      class="form-control"
                      required
                    />
                    <button class="btn btn-warning" type="submit">
                      Sửa
                    </button>
                  </form>
                </td>

                <td class="text-end fw-semibold">
                  <fmt:formatNumber
                    value="${item.lineTotal}"
                    type="number"
                    groupingUsed="true"
                  /> đ
                </td>

                <td class="text-end">
                  <form
                    method="post"
                    action="${pageContext.request.contextPath}/cart/remove"
                    onsubmit="return confirm('Xóa sản phẩm khỏi giỏ hàng?');"
                  >
                    <input
                      type="hidden"
                      name="videoId"
                      value="<c:out value='${item.videoId}' />"
                    />
                    <button class="btn btn-danger" type="submit">
                      Xóa
                    </button>
                  </form>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>

      <div class="card-footer bg-white p-4">
        <div class="d-flex flex-wrap justify-content-end align-items-center gap-4">
          <div>
            Tổng số lượng:
            <strong><c:out value="${cart.totalQuantity}" /></strong>
          </div>
          <div class="fs-5">
            Tổng tiền:
            <strong class="text-danger">
              <fmt:formatNumber
                value="${cart.totalAmount}"
                type="number"
                groupingUsed="true"
              /> đ
            </strong>
          </div>
          <a
            href="${pageContext.request.contextPath}/checkout"
            class="btn btn-success btn-lg"
          >
            Thanh toán COD
          </a>
        </div>
      </div>
    </div>
  </c:otherwise>
</c:choose>

    </main>
    <%@ include file="../fragments/footer.jspf" %>
  </body>
</html>
