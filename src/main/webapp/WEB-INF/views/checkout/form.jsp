<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!doctype html>
<html lang="vi">
  <head>
    <%@ include file="../fragments/head.jspf" %>
    <title>Thanh toán COD</title>
  </head>
  <body class="bg-light d-flex flex-column min-vh-100">
    <%@ include file="../fragments/user-header.jspf" %>
    <main class="container py-4 flex-grow-1">

<h1 class="h2 mb-1">Thanh toán đơn hàng</h1>
<p class="text-muted mb-4">
  Phương thức thanh toán: nhận hàng và thanh toán (COD)
</p>

<c:if test="${not empty errorMessage}">
  <div class="alert alert-danger">
    <c:out value="${errorMessage}" />
  </div>
</c:if>

<div class="row g-4">
  <div class="col-lg-7">
    <div class="card shadow-sm">
      <div class="card-header">
        <strong>Thông tin nhận hàng</strong>
      </div>
      <div class="card-body">
        <form
          method="post"
          action="${pageContext.request.contextPath}/checkout"
        >
          <div class="mb-3">
            <label for="recipientName" class="form-label">
              Họ tên người nhận
            </label>
            <input
              id="recipientName"
              name="recipientName"
              class="form-control"
              value="<c:out value='${checkoutForm.recipientName}' />"
              minlength="2"
              maxlength="100"
              required
            />
          </div>

          <div class="mb-3">
            <label for="phone" class="form-label">
              Số điện thoại
            </label>
            <input
              id="phone"
              name="phone"
              class="form-control"
              value="<c:out value='${checkoutForm.phone}' />"
              pattern="[0-9]{9,15}"
              maxlength="15"
              required
            />
          </div>

          <div class="mb-3">
            <label for="shippingAddress" class="form-label">
              Địa chỉ nhận hàng
            </label>
            <textarea
              id="shippingAddress"
              name="shippingAddress"
              class="form-control"
              rows="3"
              minlength="10"
              maxlength="300"
              required
            ><c:out value="${checkoutForm.shippingAddress}" /></textarea>
          </div>

          <div class="mb-3">
            <label for="note" class="form-label">
              Ghi chú (không bắt buộc)
            </label>
            <textarea
              id="note"
              name="note"
              class="form-control"
              rows="3"
              maxlength="500"
            ><c:out value="${checkoutForm.note}" /></textarea>
          </div>

          <div class="alert alert-info">
            Bạn sẽ thanh toán
            <strong>
              <fmt:formatNumber
                value="${cart.totalAmount}"
                type="number"
                groupingUsed="true"
              /> đ
            </strong>
            khi nhận hàng.
          </div>

          <div class="d-flex gap-2">
            <button type="submit" class="btn btn-success">
              Xác nhận đặt hàng COD
            </button>
            <a
              href="${pageContext.request.contextPath}/cart"
              class="btn btn-secondary"
            >
              Quay lại giỏ hàng
            </a>
          </div>
        </form>
      </div>
    </div>
  </div>

  <div class="col-lg-5">
    <div class="card shadow-sm">
      <div class="card-header">
        <strong>Tóm tắt đơn hàng</strong>
      </div>
      <ul class="list-group list-group-flush">
        <c:forEach items="${cart.items}" var="item">
          <li class="list-group-item d-flex justify-content-between gap-3">
            <span>
              <c:out value="${item.title}" />
              × <c:out value="${item.quantity}" />
            </span>
            <strong>
              <fmt:formatNumber
                value="${item.lineTotal}"
                type="number"
                groupingUsed="true"
              /> đ
            </strong>
          </li>
        </c:forEach>
      </ul>
      <div class="card-footer d-flex justify-content-between fs-5">
        <strong>Tổng cộng</strong>
        <strong class="text-danger">
          <fmt:formatNumber
            value="${cart.totalAmount}"
            type="number"
            groupingUsed="true"
          /> đ
        </strong>
      </div>
    </div>
  </div>
</div>

    </main>
    <%@ include file="../fragments/footer.jspf" %>
  </body>
</html>
