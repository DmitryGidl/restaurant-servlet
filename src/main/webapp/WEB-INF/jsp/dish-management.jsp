<%@include file="/WEB-INF/jspf/header.jspf" %>


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

<c:set var="sortField" value="${(empty requestScope.sortField) ? 'categpry' : requestScope.sortField}"/>
<c:set var="sortDir" value="${(empty requestScope.sortDir) ? 'creationDate' : requestScope.sortDir}"/>
<c:set var="filterCategory" value="${(empty requestScope.filterCategory) ? 'all' : requestScope.filterCategory}"/>
<c:set var="reverseSortDir" value="${(empty requestScope.reverseSortDir) ? 'desc' : requestScope.reverseSortDir}"/>
<c:set var="pageSize" value="${(empty requestScope.pageSize) ? '10' : requestScope.pageSize}"/>
<c:set var="currentPage" value="${(empty requestScope.currentPage) ? '1' : requestScope.currentPage}"/>
<c:set var="dishPaged" value="${requestScope.dishPaged}"/>

<div class="container my-2 text-light">
    <h1><fmt:message key="dish.management.headline"/></h1>

    <div class="dropdown mb-3">
        <a
                class="btn btn-outline-light dropdown-toggle btn-md myFilterDropdown" role="button"
                id="dropdownMenuLink" data-bs-toggle="dropdown" aria-expanded="false">
            <fmt:message key="dish.filter.category"/>
        </a>
        <a href="<c:url value='/admin/dishes/new'/>" class="btn btn-primary"><fmt:message key="dish.add"/></a>

        <ul class="dropdown-menu" aria-labelledby="dropdownMenuLink">
            <li>
                <c:url value="/admin/dishes" var="showAllUrl">
                    <c:param name="sortField" value="${sortField}"/>
                    <c:param name="sortDir" value="${reverseSortDir}"/>
                    <c:param name="filterCategory" value="all"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>

                <a href="${showAllUrl}"
                   class="dropdown-item"><fmt:message key="dish.filter.category.all"/></a></li>
            <li>
                <c:url value="/admin/dishes" var="burgersFilterUrl">
                    <c:param name="sortField" value="${sortField}"/>
                    <c:param name="sortDir" value="${reverseSortDir}"/>
                    <c:param name="filterCategory" value="burgers"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>
                <a href="${burgersFilterUrl}"
                   class="dropdown-item">Burgers</a></li>
            <li>
            <li>
                <c:url value="/admin/dishes" var="drinksFilterUrl">
                    <c:param name="sortField" value="${sortField}"/>
                    <c:param name="sortDir" value="${reverseSortDir}"/>
                    <c:param name="filterCategory" value="drinks"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>

                <a href="${drinksFilterUrl}"
                   class="dropdown-item">Drinks</a></li>
            <li>
                <c:url value="/admin/dishes" var="snacksFilterUrl">
                    <c:param name="sortField" value="${sortField}"/>
                    <c:param name="sortDir" value="${reverseSortDir}"/>
                    <c:param name="filterCategory" value="snacks"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>
                <a href="${snacksFilterUrl}"
                   class="dropdown-item">Snacks</a></li>
        </ul>
    </div>

    <table class="table table-hover">
        <thead>
        <tr>
            <th>
                <a class=" text-reset text-decoration-none">Id</a>
            </th>
            <th>
                <c:url value="/admin/dishes" var="nameSortUrl">
                    <c:param name="sortField" value="name"/>
                    <c:param name="sortDir" value="${reverseSortDir}"/>
                    <c:param name="filterCategory" value="${filterCategory}"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>

                <a href="${nameSortUrl}"
                   class=" text-reset"><fmt:message key="dish.name"/></a>
            </th>
            <th>
                <c:url value="/admin/dishes" var="categorySortUrl">
                    <c:param name="sortField" value="category"/>
                    <c:param name="sortDir" value="${reverseSortDir}"/>
                    <c:param name="filterCategory" value="${filterCategory}"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>

                <a href="${categorySortUrl}"
                   class="text-reset"><fmt:message key="dish.category"/></a>
            </th>
            <th>
                <c:url value="/admin/dishes" var="descriptionSortUrl">
                    <c:param name="sortField" value="description"/>
                    <c:param name="sortDir" value="${reverseSortDir}"/>
                    <c:param name="filterCategory" value="${filterCategory}"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>
                <a href="${descriptionSortUrl}"
                   class=" text-reset"><fmt:message key="dish.description"/></a>
            </th>
            <th>
                <c:url value="/admin/dishes" var="priceSortUrl">
                    <c:param name="sortField" value="price"/>
                    <c:param name="sortDir" value="${reverseSortDir}"/>
                    <c:param name="filterCategory" value="${filterCategory}"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>
                <a href="${priceSortUrl}"
                   class=" text-reset"><fmt:message key="dish.price"/></a>
            </th>
            <th><fmt:message key="table.actions"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="dish" items="${dishPaged.pageContent}">
            <tr>
                <td>${dish.id}</td>
                <td>${dish.name}</td>
                <td>${dish.category}</td>
                <td>${dish.description}</td>
                <td>${dish.price}</td>

                <td>

                    <c:url value="/admin/dishes/update" var="updateFormUrl">
                        <c:param name="id" value="${dish.id}"/>
                    </c:url>
                    <a href="${updateFormUrl}" class="btn btn-primary"><fmt:message key="dish.update.button"/></a>
                    <form method="post" action="<c:url  value='/admin/dishes/delete'/>" class="d-inline">
                        <input type="hidden" name="id" value="${dish.id}">
                        <button
                                class="btn btn-danger" type="submit"><fmt:message key="dish.delete.button"/>
                        </button>
                    </form>

                </td>
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
                        <c:url value="/admin/dishes" var="pageSizeFilterUrl5">
                            <c:param name="sortField" value="${sortField}"/>
                            <c:param name="sortDir" value="${sortDir}"/>
                            <c:param name="filterCategory" value="${filterCategory}"/>
                            <c:param name="pageSize" value="5"/>
                        </c:url>
                        <a href="${pageSizeFilterUrl5}"
                           class="dropdown-item">5</a></li>
                    <li>
                        <c:url value="/admin/dishes" var="pageSizeFilterUrl10">
                            <c:param name="sortField" value="${sortField}"/>
                            <c:param name="sortDir" value="${sortDir}"/>
                            <c:param name="filterCategory" value="${filterCategory}"/>
                            <c:param name="pageSize" value="10"/>
                        </c:url>
                        <a href="${pageSizeFilterUrl10}"
                           class="dropdown-item">10</a></li>
                    <li>
                        <c:url value="/admin/dishes" var="pageSizeFilterUrl20">
                            <c:param name="sortField" value="${sortField}"/>
                            <c:param name="sortDir" value="${sortDir}"/>
                            <c:param name="filterCategory" value="${filterCategory}"/>
                            <c:param name="pageSize" value="20"/>
                        </c:url>
                        <a href="${pageSizeFilterUrl20}"
                           class="dropdown-item">20</a></li>
                    <li>
                        <c:url value="/admin/dishes" var="pageSizeFilterUrl30">
                            <c:param name="sortField" value="${sortField}"/>
                            <c:param name="sortDir" value="${sortDir}"/>
                            <c:param name="filterCategory" value="${filterCategory}"/>
                            <c:param name="pageSize" value="30"/>
                        </c:url>
                        <a href="${pageSizeFilterUrl30}"
                           class="dropdown-item">30</a></li>
                </ul>

            </div>
        </div>

        <nav aria-label="Page navigation" class="paging col">
            <c:if test="${dishPaged.paging.totalPages > 1}">
                <ul class="pagination">
                    <li class="page-item ${!dishPaged.paging.prevEnabled ? 'disabled' : ''}">

                        <c:url value="/admin/dishes" var="previousLink">
                            <c:param name="sortField" value="${sortField}"/>
                            <c:param name="sortDir" value="${sortDir}"/>
                            <c:param name="filterCategory" value="${filterCategory}"/>
                            <c:param name="pageSize" value="${pageSize}"/>
                            <c:param name="currentPage" value="${currentPage-1}"/>
                        </c:url>
                        <a href="${previousLink}" class="page-link" tabindex="-1"><fmt:message key="pagination.previous"/></a>
                    </li>
                    <c:forEach var="item" items="${dishPaged.paging.items}">

                        <c:if test="${item.pageItemType.name() == 'PAGE'}">
                            <li class="page-item ${item.index == dishPaged.paging.pageNumber ? 'active' : ''}">
                                <c:url value="/admin/dishes" var="paginationUrl">
                                    <c:param name="sortField" value="${sortField}"/>
                                    <c:param name="sortDir" value="${sortDir}"/>
                                    <c:param name="filterCategory" value="${filterCategory}"/>
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

                    <li class="page-item ${!dishPaged.paging.nextEnabled ? 'disabled' : ''}">
                        <c:url value="/admin/dishes" var="nextLink">
                            <c:param name="sortField" value="${sortField}"/>
                            <c:param name="sortDir" value="${sortDir}"/>
                            <c:param name="filterCategory" value="${filterCategory}"/>
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
