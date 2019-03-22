<div class="tab-pane" id="portlet_tab_application">

</div>
<div class="tab-pane" id="portlet_tab_review">
  <div class="m-grid">
    <div class="m-grid-row">
      <div id="fileTree" class="m-grid-col  m-grid-col-md-3">
        <ul>
          <li data-jstree='{ "opened" : true }'> 申请文件
            <ul id="01"></ul>
          </li>
          <li> 中间文件
            <ul id="02"></ul>
          </li>
          <li> 通知书
            <ul id="03"></ul>
          </li>
          <li> 复审文件
            <ul id="04"></ul>
          </li>
        </ul>
      </div>
      <div class="m-grid-col  m-grid-col-md-9">
        <div class="flipbook-viewport">
          <div class="container">
            <div class="flipbook">

            </div>
          </div>

        </div>
      </div>
    </div>
  </div>
</div>
<div class="tab-pane" id="portlet_tab_cost">
  <div class="portlet light">
    <div class="portlet-title">
      <div class="caption">
        <span class="caption-subject bold font-grey-gallery uppercase"> 应缴费信息 </span>
      </div>
      <div class="tools">
        <a href="" class="collapse"> </a>
      </div>
    </div>
    <div class="portlet-body">
      <table class="table table-bordered table-striped table-condensed flip-content">
        <thead class="flip-content">
        <tr>
          <th> 费用种类</th>
          <th> 应缴金额</th>
          <th> 缴费截止日</th>
          <th> 费用状态</th>
        </tr>
        </thead>
        <tbody id="payable"></tbody>
      </table>
    </div>
  </div>
  <div class="portlet light">
    <div class="portlet-title">
      <div class="caption">
        <span class="caption-subject bold font-grey-gallery uppercase"> 已缴费信息 </span>
      </div>
      <div class="tools">
        <a href="" class="collapse"> </a>
      </div>
    </div>
    <div class="portlet-body">
      <table class="table table-bordered table-striped table-condensed flip-content">
        <thead class="flip-content">
        <tr>
          <th> 缴费种类</th>
          <th> 缴费金额</th>
          <th> 缴费日期</th>
          <th> 缴费人姓名</th>
          <th> 收据号</th>
        </tr>
        </thead>
        <tbody id="paid"></tbody>
      </table>
    </div>
  </div>
  <div class="portlet light">
    <div class="portlet-title">
      <div class="caption">
        <span class="caption-subject bold font-grey-gallery uppercase"> 退费信息 </span>
      </div>
      <div class="tools">
        <a href="" class="collapse"> </a>
      </div>
    </div>
    <div class="portlet-body">
      <table class="table table-bordered table-striped table-condensed flip-content">
        <thead class="flip-content">
        <tr>
          <th> 退费种类</th>
          <th> 退费金额</th>
          <th> 退费日期</th>
          <th> 收款人姓名</th>
          <th> 收据号</th>
        </tr>
        </thead>
        <tbody id="refund"></tbody>
      </table>
    </div>
  </div>
  <div class="portlet light">
    <div class="portlet-title">
      <div class="caption">
        <span class="caption-subject bold font-grey-gallery uppercase"> 滞纳金信息 </span>
      </div>
      <div class="tools">
        <a href="" class="collapse"> </a>
      </div>
    </div>
    <div class="portlet-body">
      <table class="table table-bordered table-striped table-condensed flip-content">
        <thead class="flip-content">
        <tr>
          <th> 缴费时间</th>
          <th> 当前年费金额</th>
          <th> 应交滞纳金额</th>
          <th> 总计</th>
        </tr>
        </thead>
        <tbody id="forfeit"></tbody>
      </table>
    </div>
  </div>
  <div class="portlet light">
    <div class="portlet-title">
      <div class="caption">
        <span class="caption-subject bold font-grey-gallery uppercase"> 收据发文信息 </span>
      </div>
      <div class="tools">
        <a href="" class="collapse"> </a>
      </div>
    </div>
    <div class="portlet-body">
      <table class="table table-bordered table-striped table-condensed flip-content">
        <thead class="flip-content">
        <tr>
          <th> 费用种类</th>
          <th> 缴费金额</th>
          <th> 缴费人姓名</th>
          <th> 缴费时间</th>
          <th> 收据号</th>
          <th> 收据抬头</th>
          <th> 收据邮寄地址</th>
          <th> 汇款日期</th>
          <th> 是否寄出</th>
          <th> 发文日期</th>
          <th> 挂号号码</th>
          <th> 退款汇出日期</th>
        </tr>
        </thead>
        <tbody id="receipt"></tbody>
      </table>
    </div>
  </div>
</div>
<div class="tab-pane" id="portlet_tab_post">
  <div class="portlet light">
    <div class="portlet-title">
      <div class="caption">
        <span class="caption-subject bold font-grey-gallery uppercase"> 通知书发文 </span>
      </div>
      <div class="tools">
        <a href="" class="collapse"> </a>
      </div>
    </div>
    <div class="portlet-body">
      <table class="table table-bordered table-striped table-condensed flip-content">
        <thead class="flip-content">
        <tr>
          <th> 通知书名称</th>
          <th> 发文日</th>
          <th> 收件人姓名</th>
          <th> 收件人邮编</th>
          <th> 下载时间</th>
          <th> 下载IP地址</th>
          <th> 发文方式</th>
        </tr>
        </thead>
        <tbody id="notice"></tbody>
      </table>
    </div>
  </div>
  <div class="portlet light">
    <div class="portlet-title">
      <div class="caption">
        <span class="caption-subject bold font-grey-gallery uppercase"> 专利证书 </span>
      </div>
      <div class="tools">
        <a href="" class="collapse"> </a>
      </div>
    </div>
    <div class="portlet-body">
      <table class="table table-bordered table-striped table-condensed flip-content">
        <thead class="flip-content">
        <tr>
          <th> 发文日</th>
          <th> 收件人姓名</th>
          <th> 收件人邮编</th>
        </tr>
        </thead>
        <tbody id="patentCertificate"></tbody>
      </table>
    </div>
  </div>
  <div class="portlet light">
    <div class="portlet-title">
      <div class="caption">
        <span class="caption-subject bold font-grey-gallery uppercase"> 退信 </span>
      </div>
      <div class="tools">
        <a href="" class="collapse"> </a>
      </div>
    </div>
    <div class="portlet-body">
      <table class="table table-bordered table-striped table-condensed flip-content">
        <thead class="flip-content">
        <tr>
          <th> 退信种类名称</th>
          <th> 原收件人姓名</th>
          <th> 原通知书发文日</th>
          <th> 退信原因</th>
          <th> 重发收件人姓名</th>
          <th> 重发通知书发文日</th>
          <th> 公示送达卷期号</th>
          <th> 公示送达日期</th>
        </tr>
        </thead>
        <tbody id="bounce"></tbody>
      </table>
    </div>
  </div>
</div>
<div class="tab-pane" id="portlet_tab_announce">
  <div class="portlet light">
    <div class="portlet-title">
      <div class="caption">
        <span class="caption-subject bold font-grey-gallery uppercase"> 发明公布/授权公告 </span>
      </div>
      <div class="tools">
        <a href="" class="collapse"> </a>
      </div>
    </div>
    <div class="portlet-body">
      <table class="table table-bordered table-striped table-condensed flip-content">
        <thead class="flip-content">
        <tr>
          <th> 公告（公布）号</th>
          <th> 公告类型</th>
          <th> 卷期号</th>
          <th> 公告（公布）日</th>
        </tr>
        </thead>
        <tbody id="publish"></tbody>
      </table>
    </div>
  </div>
  <div class="portlet light">
    <div class="portlet-title">
      <div class="caption">
        <span class="caption-subject bold font-grey-gallery uppercase"> 事务公告 </span>
      </div>
      <div class="tools">
        <a href="" class="collapse"> </a>
      </div>
    </div>
    <div class="portlet-body">
      <table class="table table-bordered table-striped table-condensed flip-content">
        <thead class="flip-content">
        <tr>
          <th> 事务公告类型</th>
          <th> 公告卷期号</th>
          <th> 事务公告日</th>
        </tr>
        </thead>
        <tbody id="transactionPublish"></tbody>
      </table>
    </div>
  </div>
</div>
