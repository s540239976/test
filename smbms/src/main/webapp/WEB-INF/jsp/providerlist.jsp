<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/common/head.jsp" %>

<div class="right">
    <div class="location">
        <strong>你现在所在的位置是:</strong>
        <span>供应商管理页面</span>
    </div>
    <div class="search">
        <form method="get" action="${pageContext.request.contextPath }/provider/query">
            <input name="method" value="query" type="hidden">
            <input type="hidden" name="pageIndex" value="1"/>
            <input type="hidden" id="totalPageCount" value="${totalPageCount}"/>
            <span>供应商编码：</span>
            <input name="queryProCode" type="text" value="${queryProCode }">

            <span>供应商名称：</span>
            <input name="queryProName" type="text" value="${queryProName }">

            <input value="查 询" type="submit" id="searchbutton">
            <a href="${pageContext.request.contextPath }/provider/addProvider">添加供应商</a>
        </form>
    </div>
    <!--供应商操作表格-->
    <table class="providerTable" cellpadding="0" cellspacing="0">
        <tr class="firstTr">
            <th width="10%">供应商编码</th>
            <th width="20%">供应商名称</th>
            <th width="10%">联系人</th>
            <th width="10%">联系电话</th>
            <th width="10%">传真</th>
            <th width="10%">创建时间</th>
            <th width="30%">操作</th>
        </tr>
        <c:forEach var="provider" items="${providerList }" varStatus="status">
            <tr>
                <td>
                    <span>${provider.proCode }</span>
                </td>
                <td>
                    <span>${provider.proName }</span>
                </td>
                <td>
                    <span>${provider.proContact}</span>
                </td>
                <td>
                    <span>${provider.proPhone}</span>
                </td>
                <td>
                    <span>${provider.proFax}</span>
                </td>
                <td>
					<span>
					<fmt:formatDate value="${provider.creationDate}" pattern="yyyy-MM-dd"/>
					</span>
                </td>
                <td>
                    <span><a class="viewProvider" href="javascript:;"
                             proid=${provider.id } proname=${provider.proName }><img
                            src="${pageContext.request.contextPath }/statics/images/read.png" alt="查看" title="查看"/></a></span>
                    <span><a class="modifyProvider" href="javascript:;"
                             proid=${provider.id } proname=${provider.proName }><img
                            src="${pageContext.request.contextPath }/statics/images/xiugai.png" alt="修改"
                            title="修改"/></a></span>
                    <span><a class="deleteProvider" href="javascript:;"
                             proid=${provider.id } proname=${provider.proName }><img
                            src="${pageContext.request.contextPath }/statics/images/schu.png" alt="删除" title="删除"/></a></span>
                </td>
            </tr>
        </c:forEach>
    </table>
    <div class="providerView">
        <p><strong>供应商编码：</strong><span id="proCode"></span></p>
        <p><strong>供应商名称：</strong><span id="proName"></span></p>
        <p><strong>联系人：</strong><span id="contact"></span></p>
        <p><strong>联系电话：</strong><span id="phone"></span></p>
        <p><strong>传真：</strong><span id="profix"></span></p>
        <p><strong>描述：</strong><span id="proDesc"></span></p>
    </div>

    <c:import url="rollpage.jsp">
        <c:param name="totalCount" value="${totalCount}"/>
        <c:param name="currentPageNo" value="${currentPageNo}"/>
        <c:param name="totalPageCount" value="${totalPageCount}"/>
    </c:import>
</div>
</section>

<!--点击删除按钮后弹出的页面-->
<div class="zhezhao"></div>
<div class="remove" id="removeProv">
    <div class="removerChid">
        <h2>提示</h2>
        <div class="removeMain">
            <p>你确定要删除该供应商吗？</p>
            <a href="#" id="yes">确定</a>
            <a href="#" id="no">取消</a>
        </div>
    </div>
</div>

<%@include file="/WEB-INF/jsp/common/foot.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath }/statics/js/providerlist.js"></script>
