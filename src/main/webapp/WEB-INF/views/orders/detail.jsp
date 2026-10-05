<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!doctype html>
<html lang="vi">
  <head>
    <%@ include file="../fragments/head.jspf" %>
    <title>Chi tiết đơn hàng</title>
  </head>
  <body class="bg-light d-flex flex-column min-vh-100">
    <%@ include file="../fragments/user-header.jspf" %>
    <main class="container py-4 flex-grow-1">

<c:if test="${not empty successMessage}">
  <div class="alert alert-success shadow-sm">
    <h1 class="h4">Đặt hàng thành công</h1>
    <p class="mb-0">
      Đơn COD đã được ghi nhận và giỏ hàng đã được làm trống.
    </p>
  </div>
</c:if>

<c:choose>
  <c:when test="${not empty errorMessage}">
    <div class="alert alert-danger">
      <c:out value="${errorMessage}" />
    </div>
    <a
      href="${pageContext.request.contextPath}/orders"
      class="btn btn-secondary"
    >
      Quay lại lịch sử
    </a>
  </c:when>

  <c:otherwise>
    <div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-4">
      <div>
        <h1 class="h2 mb-1">
          Đơn hàng
          <c:out value="${orderDetail.order.orderCode}" />
        </h1>
        <div class="text-muted">
          Đặt lúc
          <c:out value="${orderDetail.order.createdAtDisplay}" />
        </div>
      </div>
      <span class="badge fs-6 ${orderDetail.order.status.badgeClass}">
        <c:out value="${orderDetail.order.status.label}" />
      </span>
    </div>

    <div class="row g-4 mb-4">
      <div class="col-lg-7">
        <div class="card shadow-sm h-100">
          <div class="card-header">
            <strong>Sản phẩm đã đặt</strong>
          </div>
          <div class="table-responsive">
            <table class="table align-middle mb-0">
              <thead>
                <tr>
                  <th>Sản phẩm</th>
                  <th class="text-end">Đơn giá</th>
                  <th class="text-center">SL</th>
                  <th class="text-end">Thành tiền</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach items="${orderDetail.items}" var="item">
                  <tr>
                    <td>
                      <div class="fw-semibold">
                        <c:out value="${item.videoTitle}" />
                      </div>
                      <div class="small text-muted">
                        <c:out value="${item.videoId}" />
                      </div>
                    </td>
                    <td class="text-end">
                      <fmt:formatNumber
                        value="${item.unitPrice}"
                        type="number"
                        groupingUsed="true"
                      /> đ
                    </td>
                    <td class="text-center">
                      <c:out value="${item.quantity}" />
                    </td>
                    <td class="text-end fw-semibold">
                      <fmt:formatNumber
                        value="${item.lineTotal}"
                        type="number"
                        groupingUsed="true"
                      /> đ
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
              <tfoot>
                <tr class="table-light">
                  <th colspan="3" class="text-end">Tổng cộng</th>
                  <th class="text-end text-danger fs-5">
                    <fmt:formatNumber
                      value="${orderDetail.order.totalAmount}"
                      type="number"
                      groupingUsed="true"
                    /> đ
                  </th>
                </tr>
              </tfoot>
            </table>
          </div>
        </div>
      </div>

      <div class="col-lg-5">
        <div class="card shadow-sm h-100">
          <div class="card-header">
            <strong>Thông tin giao hàng</strong>
          </div>
          <div class="card-body">
            <dl class="row mb-0">
              <dt class="col-sm-5">Người nhận</dt>
              <dd class="col-sm-7">
                <c:out value="${orderDetail.order.recipientName}" />
              </dd>

              <dt class="col-sm-5">Điện thoại</dt>
              <dd class="col-sm-7">
                <c:out value="${orderDetail.order.phone}" />
              </dd>

              <dt class="col-sm-5">Địa chỉ</dt>
              <dd class="col-sm-7">
                <c:out value="${orderDetail.order.shippingAddress}" />
              </dd>

              <dt class="col-sm-5">Thanh toán</dt>
              <dd class="col-sm-7">
                COD - thanh toán khi nhận hàng
              </dd>

              <dt class="col-sm-5">Ghi chú</dt>
              <dd class="col-sm-7">
                <c:choose>
                  <c:when test="${empty orderDetail.order.note}">
                    Không có
                  </c:when>
                  <c:otherwise>
                    <c:out value="${orderDetail.order.note}" />
                  </c:otherwise>
                </c:choose>
              </dd>

              <dt class="col-sm-5">Cập nhật</dt>
              <dd class="col-sm-7 mb-0">
                <c:out value="${orderDetail.order.updatedAtDisplay}" />
              </dd>
            </dl>
          </div>
        </div>
      </div>
    </div>

    <a
      href="${pageContext.request.contextPath}/orders"
      class="btn btn-outline-primary"
    >
      ← Lịch sử đặt hàng
    </a>
  </c:otherwise>
</c:choose>

    </main>
    <%@ include file="../fragments/footer.jspf" %>
  </body>
</html>
