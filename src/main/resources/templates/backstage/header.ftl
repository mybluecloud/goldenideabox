<div class="page-wrapper-row">
  <div class="page-wrapper-top">
    <!-- BEGIN HEADER -->
    <div class="page-header">
      <!-- BEGIN HEADER TOP -->
      <div class="page-header-top">
        <div class="container-fluid">
          <!-- BEGIN LOGO -->
          <div class="page-logo">
            <a href="${request.contextPath}/index">
              <img src="${request.contextPath}/layouts/layout/backstage/img/logo-default.jpg" alt="logo" class="logo-default">
            </a>
          </div>
          <!-- END LOGO -->
          <!-- BEGIN RESPONSIVE MENU TOGGLER -->
          <a href="javascript:;" class="menu-toggler"></a>
          <!-- END RESPONSIVE MENU TOGGLER -->
          <!-- BEGIN TOP NAVIGATION MENU -->
          <div class="top-menu">
            <ul class="nav navbar-nav pull-right">
              <!-- BEGIN USER LOGIN DROPDOWN -->
              <li class="dropdown dropdown-user dropdown-dark">
                <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown"
                   data-hover="dropdown" data-close-others="true">
                  <img id="user-image" alt="" class="img-circle"  src="${request.contextPath}<@shiro.principal property='image'/>">
                  <span id="user-name" class="username username-hide-mobile"><@shiro.principal property="name"/></span>
                  <input type="hidden" id="user-id" value="<@shiro.principal property='id'/>">
                </a>
                <ul class="dropdown-menu dropdown-menu-default">
                  <li>
                    <a href="${request.contextPath}/userinfo">
                      <i class="icon-user"></i> 个人资料 </a>
                  </li>

                  <li class="divider"></li>

                  <li>
                    <a href="${request.contextPath}/logout">
                      <i class="icon-key"></i> 注销 </a>
                  </li>
                </ul>
              </li>
              <!-- END USER LOGIN DROPDOWN -->
              <!-- BEGIN QUICK SIDEBAR TOGGLER -->
              <li class="dropdown dropdown-extended  quick-sidebar-toggler">
                <span class="sr-only">消息会话</span>
                <i class="icon-bell"></i>
                <span class="badge badge-default" id="msgnum"></span>
              </li>
              <!-- END QUICK SIDEBAR TOGGLER -->
            </ul>
          </div>
          <!-- END TOP NAVIGATION MENU -->
        </div>
      </div>
      <!-- END HEADER TOP -->
      <!-- BEGIN HEADER MENU -->
      <div class="page-header-menu">
        <div class="container-fluid">

          <!-- BEGIN MEGA MENU -->
          <!-- DOC: Apply "hor-menu-light" class after the "hor-menu" class below to have a horizontal menu with white background -->
          <!-- DOC: Remove data-hover="dropdown" and data-close-others="true" attributes below to disable the dropdown opening on mouse hover -->
          <div class="hor-menu  ">
            <ul class="nav navbar-nav">
              <li aria-haspopup="true" class="menu-dropdown classic-menu-dropdown ">
                <a href="${request.contextPath}/index"> 首页
                  <span class="arrow"></span>
                </a>

              </li>
              <@shiro.hasPermission name="/permit">
              <li aria-haspopup="true" class="menu-dropdown classic-menu-dropdown ">
                <a href="javascript:;"> 权限管理
                  <span class="arrow"></span>
                </a>
                <ul class="dropdown-menu pull-left">
                  <@shiro.hasPermission name="/permit/user">
                  <li aria-haspopup="true" class=" ">
                    <a href="${request.contextPath}/user" class="nav-link  "> 用户管理 </a>
                  </li>
                  </@shiro.hasPermission>
                <@shiro.hasPermission name="/permit/role">
                  <li aria-haspopup="true" class=" ">
                    <a href="${request.contextPath}/role" class="nav-link  "> 角色管理 </a>
                  </li>
                </@shiro.hasPermission>
                <@shiro.hasPermission name="/permit/resource">
                  <li aria-haspopup="true" class=" ">
                    <a href="${request.contextPath}/resource" class="nav-link  "> 资源管理 </a>
                  </li>
                </@shiro.hasPermission>
                </ul>
              </li>
              </@shiro.hasPermission>
              <@shiro.hasPermission name="/patent">
              <li aria-haspopup="true" class="menu-dropdown classic-menu-dropdown ">
                <a href="javascript:;"> 案件管理
                  <span class="arrow"></span>
                </a>
                <ul class="dropdown-menu pull-left">
                  <@shiro.hasPermission name="/patent/appoint">
                  <li aria-haspopup="true" class=" ">
                    <a href="${request.contextPath}/patent/appointpatent" class="nav-link  "> 案件指派 </a>
                  </li>
                  </@shiro.hasPermission>
                  <@shiro.hasPermission name="/patent/claim">
                  <li aria-haspopup="true" class=" ">
                    <a href="${request.contextPath}/patent/claimpatent" class="nav-link  "> 案件认领 </a>
                  </li>
                  </@shiro.hasPermission>
                  <@shiro.hasPermission name="/patent/view">
                  <li aria-haspopup="true" class=" ">
                    <a href="${request.contextPath}/patent/patentlist" class="nav-link  "> 案件列表 </a>
                  </li>
                  <li aria-haspopup="true" class=" ">
                    <a href="${request.contextPath}/special/import" class="nav-link  "> 案件导入 </a>
                  </li>
                  </@shiro.hasPermission>
                </ul>
              </li>
              </@shiro.hasPermission>
              <@shiro.hasPermission name="/template">
              <li aria-haspopup="true" class="menu-dropdown classic-menu-dropdown ">
                <a href="javascript:;"> 模板管理
                  <span class="arrow"></span>
                </a>
                <ul class="dropdown-menu pull-left">
                  <li aria-haspopup="true" class=" ">
                    <a href="${request.contextPath}/template" class="nav-link  "> 我的模板 </a>
                  </li>
                </ul>
              </li>
              </@shiro.hasPermission>
              <li aria-haspopup="true" class="menu-dropdown classic-menu-dropdown ">
                <a href="javascript:;"> 图表管理
                  <span class="arrow"></span>
                </a>
                <ul class="dropdown-menu pull-left">
                  <li aria-haspopup="true" class=" ">
                    <a href="${request.contextPath}/chart/sectorChart" class="nav-link  "> 扇形图 </a>
                  </li>
                  <li aria-haspopup="true" class=" ">
                    <a href="${request.contextPath}/chart/histogram" class="nav-link  "> 柱形图 </a>
                  </li>
                  <li aria-haspopup="true" class=" ">
                    <a href="${request.contextPath}/chart/lineChart" class="nav-link  "> 折线图 </a>
                  </li>
                </ul>
              </li>
              <@shiro.hasPermission name="/cpquery">
              <li aria-haspopup="true" class="menu-dropdown classic-menu-dropdown ">
                <a href="javascript:;"> 自动化查询
                  <span class="arrow"></span>
                </a>
                <ul class="dropdown-menu pull-left">
                  <li aria-haspopup="true" class=" ">
                    <a href="${request.contextPath}/cpquery/cpquerylist" class="nav-link  "> 官方状态查询 </a>
                  </li>
                </ul>
              </li>
              </@shiro.hasPermission>
            </ul>
          </div>
          <!-- END MEGA MENU -->
        </div>
      </div>
      <!-- END HEADER MENU -->
    </div>
    <!-- END HEADER -->
  </div>
</div>