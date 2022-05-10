<%@include file="/WEB-INF/jspf/header.jspf" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix = "myTag" tagdir="/WEB-INF/tags" %>


<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Menu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
            crossorigin="anonymous"></script>


    <link rel="stylesheet" type="text/css" href="<c:url value='/css/table-manegement.css'/>"/>


</head>
<body>

<c:set var="sortField" value="${(empty requestScope.sortField) ? 'creation_date_time' : requestScope.sortField}"/>
<c:set var="sortDir" value="${(empty requestScope.sortDir) ? 'desc' : requestScope.sortDir}"/>
<c:set var="filterStatus" value="${(empty requestScope.filterStatus) ? 'all' : requestScope.filterStatus}"/>
<c:set var="reverseSortDir" value="${(empty requestScope.reverseSortDir) ? 'asc' : requestScope.reverseSortDir}"/>
<c:set var="pageSize" value="${(empty requestScope.pageSize) ? '10' : requestScope.pageSize}"/>
<c:set var="currentPage" value="${(empty requestScope.currentPage) ? '1' : requestScope.currentPage}"/>
<c:set var="orderPaged" value="${requestScope.orderPaged}"/>

<div class="container my-2 text-light">
    <h1><fmt:message key="order.history.headline"/></h1>

    <div class="dropdown mb-3">
        <a
                class="btn btn-outline-light dropdown-toggle btn-md myFilterDropdown" role="button"
                id="dropdownMenuLink" data-bs-toggle="dropdown" aria-expanded="false">
            <fmt:message key="order.filter.status"/>
        </a>

        <ul class="dropdown-menu" aria-labelledby="dropdownMenuLink">
            <li>
                <c:url value="/orders/my-history" var="showAllUrl">
                    <c:param name="sortField" value="${sortField}"/>
                    <c:param name="sortDir" value="${reverseSortDir}"/>
                    <c:param name="filterStatus" value="all"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>

                <a href="${showAllUrl}"
                   class="dropdown-item"><fmt:message key="order.show.all"/></a></li>
            <li>
                <c:url value="/orders/my-history" var="burgersFilterUrl">
                    <c:param name="sortField" value="${sortField}"/>
                    <c:param name="sortDir" value="${reverseSortDir}"/>
                    <c:param name="filterStatus" value="active"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>
                <a href="${burgersFilterUrl}"
                   class="dropdown-item"><fmt:message key="order.show.active"/></a></li>
            <li>
            <li>
                <c:url value="/orders/my-history" var="drinksFilterUrl">
                    <c:param name="sortField" value="${sortField}"/>
                    <c:param name="sortDir" value="${reverseSortDir}"/>
                    <c:param name="filterStatus" value="pending"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>

                <a href="${drinksFilterUrl}"
                   class="dropdown-item">Pending</a></li>
            <li>
                <c:url value="/orders/my-history" var="snacksFilterUrl">
                    <c:param name="sortField" value="${sortField}"/>
                    <c:param name="sortDir" value="${reverseSortDir}"/>
                    <c:param name="filterStatus" value="cooking"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>
                <a href="${snacksFilterUrl}"
                   class="dropdown-item">Cooking</a></li>

            <li>
                <c:url value="/orders/my-history" var="snacksFilterUrl">
                    <c:param name="sortField" value="${sortField}"/>
                    <c:param name="sortDir" value="${reverseSortDir}"/>
                    <c:param name="filterStatus" value="delivering"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>
                <a href="${snacksFilterUrl}"
                   class="dropdown-item">Delivering</a></li>

            <li>
                <c:url value="/orders/my-history" var="snacksFilterUrl">
                    <c:param name="sortField" value="${sortField}"/>
                    <c:param name="sortDir" value="${reverseSortDir}"/>
                    <c:param name="filterStatus" value="completed"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>
                <a href="${snacksFilterUrl}"
                   class="dropdown-item">Completed</a></li>

            <li>
                <c:url value="/orders/my-history" var="snacksFilterUrl">
                    <c:param name="sortField" value="${sortField}"/>
                    <c:param name="sortDir" value="${reverseSortDir}"/>
                    <c:param name="filterStatus" value="declined"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>
                <a href="${snacksFilterUrl}"
                   class="dropdown-item">Declined</a></li>
        </ul>
    </div>

    <table class="table table-hover">
        <thead>
        <tr>
            <th>
                <a class=" text-reset text-decoration-none">Id</a>
            </th>
            <th>
                <c:url value="/orders/my-history" var="nameSortUrl">
                    <c:param name="sortField" value="status"/>
                    <c:param name="sortDir" value="${reverseSortDir}"/>
                    <c:param name="filterStatus" value="${filterStatus}"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>

                <a href="${nameSortUrl}"
                   class=" text-reset"><fmt:message key="order.status"/></a>
            </th>
            <th>
                <c:url value="/orders/my-history" var="categorySortUrl">
                    <c:param name="sortField" value="creation_date_time"/>
                    <c:param name="sortDir" value="${reverseSortDir}"/>
                    <c:param name="filterStatus" value="${filterStatus}"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>

                <a href="${categorySortUrl}"
                   class="text-reset"><fmt:message key="order.creation.date"/></a>
            </th>
            <th>
                <c:url value="/orders/my-history" var="descriptionSortUrl">
                    <c:param name="sortField" value="update_date_time"/>
                    <c:param name="sortDir" value="${reverseSortDir}"/>
                    <c:param name="filterStatus" value="${filterStatus}"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>
                <a href="${descriptionSortUrl}"
                   class=" text-reset"><fmt:message key="order.update.date"/></a>
            </th>
            <th>Ordered Dishes</th>
            <th>
                <c:url value="/orders/my-history" var="priceSortUrl">
                    <c:param name="sortField" value="total_price"/>
                    <c:param name="sortDir" value="${reverseSortDir}"/>
                    <c:param name="filterStatus" value="${filterStatus}"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>
                <a href="${priceSortUrl}"
                   class=" text-reset"><fmt:message key="order.total.price"/></a>
            </th>
            <th>Address</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="order" items="${orderPaged.pageContent}">
            <tr>
                <td>${order.id}</td>
                <td>${order.status}</td>
                <td><myTag:LcdFormat time="${order.creationDateTime}"/></td>
                <td><myTag:LcdFormat time="${order.updateDateTime}"/></td>

                <td>
                    <c:forEach var="item" items="${order.orderItems}">
                        <p>
                            <span>${item.dishName}</span>
                            <span>-</span>
                            <span>${item.dishesOrdered}</span>
                        </p>
                    </c:forEach>
                </td>
                <td>${order.totalPrice}</td>
                <td>${order.address}</td>

            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="row">

        <div class="col">
            <div class="dropdown">
                <a
                        class="btn btn-outline-light dropdown-toggle btn-md myFilterDropdown" role="button"
                        data-bs-toggle="dropdown" aria-expanded="false">
                    <fmt:message key="page.rows"/>
                </a>

                <ul class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                    <li>
                        <c:url value="/orders/my-history" var="pageSizeFilterUrl5">
                            <c:param name="sortField" value="${sortField}"/>
                            <c:param name="sortDir" value="${sortDir}"/>
                            <c:param name="filterStatus" value="${filterStatus}"/>
                            <c:param name="pageSize" value="5"/>
                        </c:url>
                        <a href="${pageSizeFilterUrl5}"
                           class="dropdown-item">5</a></li>
                    <li>
                        <c:url value="/orders/my-history" var="pageSizeFilterUrl10">
                            <c:param name="sortField" value="${sortField}"/>
                            <c:param name="sortDir" value="${sortDir}"/>
                            <c:param name="filterStatus" value="${filterStatus}"/>
                            <c:param name="pageSize" value="10"/>
                        </c:url>
                        <a href="${pageSizeFilterUrl10}"
                           class="dropdown-item">10</a></li>
                    <li>
                        <c:url value="/orders/my-history" var="pageSizeFilterUrl20">
                            <c:param name="sortField" value="${sortField}"/>
                            <c:param name="sortDir" value="${sortDir}"/>
                            <c:param name="filterStatus" value="${filterStatus}"/>
                            <c:param name="pageSize" value="20"/>
                        </c:url>
                        <a href="${pageSizeFilterUrl20}"
                           class="dropdown-item">20</a></li>
                    <li>
                        <c:url value="/orders/my-history" var="pageSizeFilterUrl30">
                            <c:param name="sortField" value="${sortField}"/>
                            <c:param name="sortDir" value="${sortDir}"/>
                            <c:param name="filterStatus" value="${filterStatus}"/>
                            <c:param name="pageSize" value="30"/>
                        </c:url>
                        <a href="${pageSizeFilterUrl30}"
                           class="dropdown-item">30</a></li>
                </ul>

            </div>
        </div>

        <nav aria-label="Page navigation" class="paging col">
            <c:if test="${orderPaged.paging.totalPages > 1}">
                <ul class="pagination">
                    <li class="page-item ${!orderPaged.paging.prevEnabled ? 'disabled' : ''}">

                        <c:url value="/orders/my-history" var="previousLink">
                            <c:param name="sortField" value="${sortField}"/>
                            <c:param name="sortDir" value="${sortDir}"/>
                            <c:param name="filterStatus" value="${filterStatus}"/>
                            <c:param name="pageSize" value="${pageSize}"/>
                            <c:param name="currentPage" value="${currentPage-1}"/>
                        </c:url>
                        <a href="${previousLink}" class="page-link" tabindex="-1"><fmt:message key="pagination.previous"/></a>
                    </li>
                    <c:forEach var="item" items="${orderPaged.paging.items}">

                        <c:if test="${item.pageItemType.name() == 'PAGE'}">
                            <li class="page-item ${item.index == orderPaged.paging.pageNumber ? 'active' : ''}">
                                <c:url value="/orders/my-history" var="paginationUrl">
                                    <c:param name="sortField" value="${sortField}"/>
                                    <c:param name="sortDir" value="${sortDir}"/>
                                    <c:param name="filterStatus" value="${filterStatus}"/>
                                    <c:param name="pageSize" value="${pageSize}"/>
                                    <c:param name="currentPage" value="${item.index}"/>
                                </c:url>

                                <a href="${paginationUrl}" class="page-link">${item.index}</a>
                            </li>
                        </c:if>

                        <c:if test="${item.pageItemType.name() == 'DOTS'}">

                            <li class="page-item disabled">
                                <a class="page-link" href="#">...</a>
                            </li>
                        </c:if>

                    </c:forEach>

                    <li class="page-item ${!orderPaged.paging.nextEnabled ? 'disabled' : ''}">
                        <c:url value="/orders/my-history" var="nextLink">
                            <c:param name="sortField" value="${sortField}"/>
                            <c:param name="sortDir" value="${sortDir}"/>
                            <c:param name="filterStatus" value="${filterStatus}"/>
                            <c:param name="pageSize" value="${pageSize}"/>
                            <c:param name="currentPage" value="${currentPage+1}"/>
                        </c:url>
                        <a href="${nextLink}" class="page-link"><fmt:message key="pagination.next"/></a>
                    </li>
                </ul>
            </c:if>
        </nav>


    </div>
</div>


</body>
</html>
