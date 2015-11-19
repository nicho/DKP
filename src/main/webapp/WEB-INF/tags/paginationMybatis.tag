<%@tag pageEncoding="UTF-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<style>


/***************分页样式******************/
.fenye{ float:right; margin-top:10px;}
.fenye ul{ float:left; margin-left:32px; }
.fenye ul li{ float:left; margin-left:5px;padding: 4px 6px; border:1px solid #ccc;font-weight:bold; cursor:pointer; color:#999;}
.fenye ul li a{ color:#999;}
.fenye ul li.xifenye{ width:38px; text-align:center; float:left; position:relative;cursor: pointer;}
.fenye ul li .xab{ float:left; position:absolute; width:39px; border:1px solid #ccc;top:-125px; background-color: #fff; display:inline;left:-1px; width:50px; height:123px;}
.fenye ul li .xab ul{ margin-left:0; padding-bottom:0;overflow: auto;overflow-x: hidden; height:123px;}
.fenye ul li .xab ul li{ border:0; padding:4px 0px; color:#999; width:34px; margin-left:0px; text-align:center;}
</style>


<script lang="javascript" type="text/javascript">
$(document).ready(function() {
	var pagesize = '${page.pageSize}';  
	 $("#pageSizeTag").find("option[value='"+pagesize+"']").attr("selected",true);
	 
	 
      $('#pageNumTag').bind('keypress',function(event){
         if(event.keyCode == "13")    
         { 
             if($('#pageNumTag').val()<0 || $('#pageNumTag').val()>'${page.pages}')
            {
            	 alert('你输入的页码不正确');
            }  
             else
           {
            	 location.href = '?page='+$('#pageNumTag').val()+'&page.size='+$('#pageSizeTag').val()+'${searchParams}';
           }
         }
     }); 
}); 
function onChangePageSize(obj)
{ 
	location.href = '?page=1&page.size='+obj.value+'${searchParams}';
}
 
</script>
 <div  >
 <div  >
<!--分页开始--->
    <div class="fenye">
    	<ul>
        	<li id="first" ><a href="?page=1&sortType=${sortType}&page.size=${page.pageSize}&${searchParams}">首页</a></li>
            <li id="top" onClick="topclick()">
            <c:choose>
            	<c:when test="${page.prePage>0}"><a href="?page=${page.prePage}&page.size=${page.pageSize}&sortType=${sortType}&${searchParams}">上一页</a></c:when>
            	<c:otherwise><a href="#">上一页</a></c:otherwise>
            </c:choose>
            
            </li>
            <li class="xifenye" id="xifenye" style="width: 66px; ">
            	<a id="xiye"><input type="text" id="pageNumTag"  class="input" name="pageNum" value="${page.pageNum}"   style="width: 22px;height: 10px; padding: 4px 6px; margin-bottom: 0px; font-size: 12px; text-align: center;" /></a>/<a id="mo">${page.pages}</a>
                <div class="xab" id="xab" style="display:none">
                	<ul id="uljia">	
                    </ul>
                </div>
            </li>
            <li id="down"  > 
             <c:choose>
            	<c:when test="${page.nextPage>0}"><a href="?page=${page.nextPage}&page.size=${page.pageSize}&sortType=${sortType}&${searchParams}">下一页</a></c:when>
            	<c:otherwise><a href="#">下一页</a></c:otherwise>
            </c:choose>
            </li>
            <li id="last"><a href="?page=${page.pages}&sortType=${sortType}&page.size=${page.pageSize}&${searchParams}">末页</a></li>
          <li class="pages_1"> <select style="width: 57px;    padding: 0px 0px;    height: 19px;    margin-bottom: 0px;" name="pageSize" id="pageSizeTag" onchange="onChangePageSize(this)"><option id="10" value="10">10</option><option id="20" value="20">20</option><option id="30" value="30">30</option><option id="100" value="100">100</option></select> </li>
        </ul>    
    </div>
<!--分页结束--->	
</div>
 </div>
 
