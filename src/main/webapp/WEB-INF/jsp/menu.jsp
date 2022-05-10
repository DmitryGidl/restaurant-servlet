<%@include file="/WEB-INF/jspf/header.jspf" %>
<%@ taglib uri="/WEB-INF/mytags.tld" prefix="m" %>


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


    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style-menu.css"/>


</head>
<body>
<c:set var="sortField" value="${(empty requestScope.sortField) ? 'categpry' : requestScope.sortField}"/>
<c:set var="sortDir" value="${(empty requestScope.sortDir) ? 'asc' : requestScope.sortDir}"/>
<c:set var="filterCategory" value="${(empty requestScope.filterCategory) ? 'category' : requestScope.filterCategory}"/>
<c:set var="reverseSortDir" value="${(empty requestScope.reverseSortDir) ? 'desc' : requestScope.reverseSortDir}"/>
<c:set var="pageSize" value="${(empty requestScope.reverseSortDir) ? 'desc' : requestScope.pageSize}"/>

<div class="wrapper">
    <div class="title">
        <span class="slogan"><fmt:message key="menu.tagline"/></span>

        <h4><fmt:message key="menu.headline"/></h4>
    </div>


    <div class="filter-menu d-flex justify-content-end mb-4 flex-column">
        <div class="filter-menu-desc text-center">
            <p class="m-0 fs-4"><fmt:message key="menu.sort"/></p>
        </div>
        <div class="btn-group mb-3 my-button-group" role="group" aria-label="Button group with nested dropdown">

            <c:url value="/menu" var="nameSortUrl">
                <c:param name="sortField" value="name"/>
                <c:param name="sortDir" value="${reverseSortDir}"/>
                <c:param name="filterCategory" value="${filterCategory}"/>
                <c:param name="pageSize" value="${filterCategory}"/>
            </c:url>
            <a href="${nameSortUrl}" class="btn btn-outline-light mt-3 btn-lg"><fmt:message key="menu.sort.name"/></a>

            <c:url value="/menu" var="priceSortUrl">
                <c:param name="sortField" value="price"/>
                <c:param name="sortDir" value="${reverseSortDir}"/>
                <c:param name="filterCategory" value="${filterCategory}"/>
            </c:url>
            <a href="${priceSortUrl}" class="btn btn-outline-light mt-3 btn-lg"><fmt:message key="menu.sort.price"/></a>

            <c:url value="/menu" var="categorySortUrl">
                <c:param name="sortField" value="category"/>
                <c:param name="sortDir" value="${reverseSortDir}"/>
                <c:param name="filterCategory" value="${filterCategory}"/>
            </c:url>
            <a href="${categorySortUrl}" class="btn btn-outline-light mt-3 btn-lg"><fmt:message
                    key="menu.sort.category"/></a>

            <div class="btn-group" role="group">
                <button id="btnGroupDrop1" type="button"
                        class="btn btn-outline-light mt-3 btn-lg dropdown-toggle"
                        data-bs-toggle="dropdown" aria-expanded="false">
                    <fmt:message key="menu.filter.category"/>
                </button>


                <ul class="dropdown-menu" aria-labelledby="btnGroupDrop1">
                    <li>
                        <c:url value="/menu" var="showAllUrl">
                            <c:param name="sortField" value="${sortField}"/>
                            <c:param name="sortDir" value="${reverseSortDir}"/>
                            <c:param name="filterCategory" value="all"/>
                        </c:url>
                        <a href="${showAllUrl}" class="dropdown-item"><fmt:message key="menu.filter.category.all"/></a></li>
                    <li>

                        <c:url value="/menu" var="drinksFilterUrl">
                            <c:param name="sortField" value="${sortField}"/>
                            <c:param name="sortDir" value="${reverseSortDir}"/>
                            <c:param name="filterCategory" value="drinks"/>
                        </c:url>
                        <a href="${drinksFilterUrl}" class="dropdown-item">Drinks</a></li>
                    <li>

                        <c:url value="/menu" var="burgersFilterUrl">
                            <c:param name="sortField" value="${sortField}"/>
                            <c:param name="sortDir" value="${reverseSortDir}"/>
                            <c:param name="filterCategory" value="burgers"/>
                        </c:url>
                        <a href="${burgersFilterUrl}" class="dropdown-item">Burgers</a></li>
                    <li>

                        <c:url value="/menu" var="snacksFilterUrl">
                            <c:param name="sortField" value="${sortField}"/>
                            <c:param name="sortDir" value="${reverseSortDir}"/>
                            <c:param name="filterCategory" value="snacks"/>
                        </c:url>
                        <a href="${snacksFilterUrl}" class="dropdown-item">Snacks</a></li>
                </ul>
            </div>
        </div>

    </div>

    <!--MENU-->
    <form onsubmit="clearLocalStorage()"
          action="<c:url value='/orders'/>"
          method="post">

<c:if test="${requestScope.dishes.size() > 0 &&  empty requestScope.errors}">

        <div class="menu">


            <c:forEach var="dish" items="${requestScope.dishes}">

                <div class="single-menu">

                    <c:choose>
                        <c:when test="${dish.imageFileName != null}">
                            <img src="<m:printImageDirectory dishId="${dish.id}" imageFileName="${dish.imageFileName}"/>"/>
                        </c:when>
                        <c:otherwise>
                            <img src='https://via.placeholder.com/150'>
                        </c:otherwise>
                    </c:choose>
                    <div class="menu-content">
                        <h4>${dish.name}<span class="price">${dish.price}</span></h4>
                        <p class="mb-0 text-color:red">${dish.category}</p>
                        <p class="menu-description">${dish.description}</p>
                        <div class="myContainer m-1">
                            <button class="decrement" type="button" onclick="stepperDecrement(this)">-</button>
                            <input
                                    value="0"
                                    name="${'dishIdQuantityMap['}${dish.id}${']'}"
                                    type="number"
                                    min="0"
                                    max="100"
                                    step="1"
                                    readonly
                            />
                            <button class="increment" type="button" onclick="stepperIncrement(this)">+</button>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>


        <span
                class="h4 text-uppercase font-weight-bold mb-4 "><fmt:message key="menu.total.sum"/> </span>
        <span class="h2 font-weight-bold total-price"> 0</span>
        <span class=" h4 currency">₴</span>
</c:if>
        <c:if test="${requestScope.dishes.size() == 0 &&  empty requestScope.errors}">

            <div class="text-center">
                <p class="display-2 text-white"><fmt:message key="the.category.empty"/></p>
                <p class="display-2 text-white"><fmt:message key="choose.another"/></p>
                <p class="display-2 text-white">:(</p>
            </div>
        </c:if>

        <div class="d-flex justify-content-center">

            <div class="mb-3 text-center fw-bold col-8" style="font-size:1.5rem">

                <c:if test="${requestScope.errors == null && requestScope.dishes.size() > 0}">
                    <label for="address" class="form-label"><fmt:message key="menu.input.address"/></label>
                </c:if>



                <c:if test="${requestScope.errors != null}">
                    <c:forEach var="error" items="${requestScope.errors.values()}">
                        <p class="1h-sm fs-5 text-danger" style="color: red; margin: 0;"><fmt:message
                                key="${error}"/></p>

                    </c:forEach>
                    <c:url value="/menu" var="menuLink"/>

                    <a href="${menuLink}"
                       class="btn btn-outline-warning mt-2 mb-2 btn-lg"><fmt:message key="fail.try.again"/>
                    </a>
                </c:if>
                <c:if test="${requestScope.errors == null && requestScope.dishes.size() > 0}">

                    <input type="text"
                           name="address"
                           class="form-control"
                           id="address"
                           required>
                    <button type="submit" value="Submit"
                            class="btn btn-outline-light mt-3 btn-lg"><fmt:message key="menu.order.confirm"/>
                    </button>
                </c:if>

            </div>

        </div>
    </form>

</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/menu.js"></script>


</body>
</html>
