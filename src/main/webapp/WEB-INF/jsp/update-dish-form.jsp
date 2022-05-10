<%@include file="/WEB-INF/jspf/header.jspf" %>
<%@ page import="com.exampleepaam.restaurant.model.entity.Category" %>


<head>
    <meta charset="UTF-8"/>
    <meta
            name="viewport"
            content="width=device-width, initial-scale=1, shrink-to-fit=no"
    />

    <title>Add Dish</title>

    <link
            href="vendor/font-awesome-4.7/css/font-awesome.min.css"
            rel="stylesheet"
            media="all"
    />
    <link
            href="https://fonts.googleapis.com/css?family=Poppins:100,100i,200,200i,300,300i,400,400i,500,500i,600,600i,700,700i,800,800i,900,900i"
            rel="stylesheet"
    />

    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
            crossorigin="anonymous"
    />

    <!-- Main CSS-->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/add-update.css"/>

</head>

<body>
<c:set var="dish" value="${requestScope.dish}"/>

<div class="page-wrapper bg-gra-02 p-t-130 p-b-100 font-poppins">
    <div class="wrapper wrapper--w680">
        <div class="card card-4">
            <div class="card-body">
                <h2 class="title"><fmt:message key="dish.update.form"/></h2>
                <form action="<c:url value='/admin/dishes/update'/>"
                      method="post" enctype="multipart/form-data">
                    <input type="hidden" name="id" value="${dish.id}"/>


                    <div class="row row-space">
                        <div class="col-2">
                            <c:if test="${requestScope.errorName != null}">
                                <div style="color: red">
                                    <fmt:message key="${requestScope.errorName}"/>
                                </div>
                            </c:if>
                            <div class="input-group">
                                <label class="label"><fmt:message key="dish.name"/></label>
                                <input
                                        name="name"
                                        class="input--style-4"
                                        type="text"
                                        value="${dish.name}"
                                        required
                                />
                            </div>
                        </div>
                        <div class="col-2">
                            <c:if test="${requestScope.errorDescription != null}">
                                <div style="color: red">
                                    <fmt:message key="${requestScope.errorDescription}"/>
                                </div>
                            </c:if>
                            <div class="input-group">
                                <label class="label"><fmt:message key="dish.description"/></label>
                                <input
                                        name="description"
                                        class="input--style-4"
                                        type="text"
                                        value="<c:out value="${dish.description}"/>"
                                        required
                                />
                            </div>
                        </div>
                    </div>
                    <div class="row row-space">
                        <div class="col-2">
                            <c:if test="${requestScope.errorPrice != null}">
                                <div style="color: red">
                                    <fmt:message key="${requestScope.errorPrice}"/>
                                </div>
                            </c:if>
                            <div class="input-group">
                                <label class="label"><fmt:message key="dish.price"/></label>
                                <input
                                        name="price"
                                        class="input--style-4"
                                        type="number"
                                        min="0"
                                        value="${dish.price}"
                                        required

                                />
                            </div>
                        </div>
                        <div class="col-2">
                            <div class="input-group">
                                <label class="label"><fmt:message key="dish.choose.category"/></label>
                                <div>

                                    <select name="category" class="form-select" aria-label="Choose category">
                                        <c:forEach var="category" items="${Category.values()}">
                                            <option ${dish.category.equals(category)? 'selected' : ''}
                                                    value="burgers">${category}</option>
                                        </c:forEach>
                                    </select>

                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row row-space">
                        <div class="input-group d-flex flex-column">
                            <!-- ROW -->

                            <label for="image" class="form-label"><fmt:message key="dish.image"/></label>
                            <div class="mb-3">
                                <input
                                        type="file"
                                        class="form-control"
                                        id="image"
                                        name="image"
                                        accept="image/png, image/jpeg"
                                        aria-describedby="imageDescription"
                                />
                                <div id="imageDescription" class="form-text">
                                    <fmt:message key="dish.image.description"/>
                                </div>
                            </div>
                            <!-- ROW -->
                        </div>
                    </div>
                    <div class="p-t-15">
                        <button class="button-submit btn--radius-2 btn--blue" type="submit">
                            <fmt:message key="dish.update.button"/>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

</body>
</html>
