<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="" />
    <meta name="author" content="" />
    <link rel="icon" href="http://www.xuecheng.com/cdn/img/asset-favicon.ico" />
    <title>学成在线2.0</title>

    <link rel="stylesheet" href="http://www.xuecheng.com/cdn/plugins/normalize-css/normalize.css" />
    <link rel="stylesheet" href="http://www.xuecheng.com/cdn/plugins/bootstrap/dist/css/bootstrap.css" />
    <link rel="stylesheet" href="http://www.xuecheng.com/cdn/css/page-learing-article.css" />
  </head>
  <body data-spy="scroll" data-target="#articleNavbar" data-offset="150">
    <div id="container">
      <!--头部导航-->
      <header>
        <div id="headerContainer" class="learingHeader">
          <nav class="navbar">
            <div class="">
              <div class="logo">
                <img src="http://www.xuecheng.com/cdn/img/asset-logoIco.png" width="100%" alt="" />
              </div>
              <div class="nav-list">
                <ul class="nav navbar-nav">
                  <!-- TODO: 显示选中状态 -->
                  <li class="active">
                    <a
                      :href="portal_url + '/pages/learing-index.html'"
                      target="_blank"
                      >首页</a
                    >
                  </li>
                  <li>
                    <a
                      :href="portal_url + '/pages/learing-list.html'"
                      target="_blank"
                      >课程</a
                    >
                  </li>
                  <li><a href="#">职业规划</a></li>
                  <li></li>
                </ul>
              </div>

              <!-- 未登录 -->
              <div class="sign-in" v-if="!userName">
                <a :href="manage_url + '/#/login?isRegnew=0'">登录</a>
                <span>&nbsp;|&nbsp;</span>
                <a :href="manage_url + '/#/login?isRegnew=1'">注册</a>
              </div>
              <!-- 已登录 -->
              <div class="sign-in" v-else>
                <a :href="manage_url + '/#/personal/my-course'" class="personal">
                  个人中心
                </a>
                <a
                  :href="manage_url + '/#/organization/company'"
                  class="personal"
                >
                  管理 </a
                >/
                <!-- TODO: 入驻添加下拉，企业入驻和个人入驻 -->
                <a
                  :href="manage_url + '/#/entering/entering?type=0'"
                  class="personal"
                >
                  入驻
                  <span class="personalIco"></span>
                </a>
                <span class="personalIco"></span>
                <a href="javascript:;" class="myInfo">
                  <img src="http://www.xuecheng.com/cdn/img/asset-myImg.jpg" alt />
                  <span v-text="userName"></span>
                </a>
              </div>
              <div class="starch">
                <input
                  type="text"
                  id="inputSearch"
                  class="input-search"
                  placeholder="输入查询关键词"
                /><input id="btnQuery" type="submit" class="search-buttom" />
              </div>
            </div>
          </nav>
        </div>
      </header>

      <!-- 课程信息 -->
      <div class="article-banner">
        <div class="banner-bg"></div>
        <div class="banner-info">
          <div class="banner-left">
            <input
              type="hidden"
              name="coursePubId"
              value="${coursePub.id!''}"
            />
            <input
              type="hidden"
              name="courseId"
              value="${coursePub.courseId!''}"
            />
            <p>${coursePub.mtName!''} <span>\ ${coursePub.stName!''}</span></p>
            <p class="tit">${coursePub.name}</p>
            <p class="pic">
              <span class="new-pic">特惠价格￥${courseMarket.price!''}</span>
              <span class="old-pic">原价￥1999</span>
            </p>
            <p class="info">
              <a :href="courseProgress.url" v-text="courseProgress.text"></a>
              <span><em>难度等级</em>${coursePub.grade!''}</span>
              <span><em>课程时长</em>...</span>
              <span><em>评分</em>{{ starRank }}分</span>
              <span><em>授课模式</em>
              <#list courseTeachModeEnums as ctm>
              <#if coursePub.teachmode?? && coursePub.teachmode == ctm.code>${ctm.desc}</#if>
              </#list>
              </span>
            </p>
          </div>
          <div class="banner-rit">
            <p><img src="${coursePub.pic!''}" alt="" /></p>
            <p class="vid-act">
              <span> <i class="i-heart"></i>收藏 23 </span>
              <span>分享 <i class="i-weixin"></i><i class="i-qq"></i></span>
            </p>
          </div>
        </div>
      </div>

      <!-- 课程详情 -->
      <div class="article-cont">
        <div class="tit-list">
          <a
            href="javascript:;"
            id="articleClass"
            v-for="(item, index) in articleTapList"
            v-text="item"
            :class="articleTapIndex === index ? 'active' : ''"
            @click="handleClickArticleTap(index)"
            >课程介绍</a
          >
        </div>
        <div class="article-box">
          <div class="articleClass" v-show="articleTapIndex === 0">
            <div class="rit-title">评价</div>
            <div class="article-cont">
              <div class="article-left-box">
                <div class="content">
                  <div class="content-com about">
                    <div class="title"><span>课程介绍</span></div>
                    <div class="cont cktop">
                      <div class="drop-down">
                        <p>
                          ${(coursePub.description)!''}
                        </p>
                      </div>
                      <span class="on-off"
                        >更多 <i class="i-chevron-bot"></i
                      ></span>
                    </div>
                  </div>
                  <div class="content-com suit">
                    <div class="title"><span>适用人群</span></div>
                    <div class="cont cktop">
                      <div class="drop-down">
                        <p>
                          ${(coursePub.users)!''}
                        </p>
                      </div>
                      <span class="on-off"
                        >更多 <i class="i-chevron-bot"></i
                      ></span>
                    </div>
                  </div>
                  <div class="content-com course">
                    <div class="title"><span>课程制作</span></div>
                    <div class="cont">
                      <div class="img-box">
                        <img src="http://www.xuecheng.com/cdn/img/widget-pic.png" alt="" />
                      </div>
                      <div class="info-box">
                        <p class="name">教学方：<em>毛老师</em></p>
                        <p class="lab">高级前端开发工程师 10年开发经验</p>
                        <p class="info">
                          JavaEE开发与教学多年，精通JavaEE技术体系，对流行框架JQuery、DWR、Struts1/2，Hibernate，Spring，MyBatis、JBPM、Lucene等有深入研究。授课逻辑严谨，条理清晰，注重学生独立解决问题的能力。
                        </p>
                        <p><span>难度等级</span>中级</p>
                        <p><span>课程时长</span>8-16小时/周，共4周</p>
                        <p>
                          <span>如何通过</span
                          >通过所有的作业及考核，作业共4份，考核为一次终极考核
                        </p>
                        <p>
                          <span>用户评分</span>平均用户评分 <em>4.9</em>
                          <a href="#">查看全部评价</a>
                        </p>
                        <p>
                          <span>课程价格</span>特惠价格<em>￥999</em>
                          <i> 原价1999 </i>
                        </p>
                      </div>
                    </div>
                  </div>
                  <div class="content-com prob">
                    <div class="title"><span>常见问题</span></div>
                    <div class="cont">
                      <ul>
                        <li class="item">
                          <span class="on-off"
                            ><i class="i-chevron-bot"></i>
                            我什么时候能够访问课程视频与作业？</span
                          >
                          <div class="drop-down">
                            <p>
                              课程安排灵活，课程费用支付提供180天全程准入和资格证书。自定进度课程建议的最后期限，但你不会受到惩罚错过期限，只要你赚你的证书在180天内。以会话为基础的课程可能要求你在截止日期前保持正轨，但如果你落后了，你可以切换到以后的会议，你完成的任何工作将与你转移。
                            </p>
                          </div>
                        </li>
                        <li class="item">
                          <span class="on-off"
                            ><i class="i-chevron-bot"></i>
                            如何需要额外的时间来完成课程会怎么样？</span
                          >
                          <div class="drop-down">
                            <p>
                              课程安排灵活，课程费用支付提供180天全程准入和资格证书。自定进度课程建议的最后期限，但你不会受到惩罚错过期限，只要你赚你的证书在180天内。以会话为基础的课程可能要求你在截止日期前保持正轨，但如果你落后了，你可以切换到以后的会议，你完成的任何工作将与你转移。
                            </p>
                          </div>
                        </li>
                        <li class="item">
                          <span class="on-off"
                            ><i class="i-chevron-bot"></i>
                            我支付次课程之后会得到什么？</span
                          >
                          <div class="drop-down">
                            <p>
                              课程安排灵活，课程费用支付提供180天全程准入和资格证书。自定进度课程建议的最后期限，但你不会受到惩罚错过期限，只要你赚你的证书在180天内。以会话为基础的课程可能要求你在截止日期前保持正轨，但如果你落后了，你可以切换到以后的会议，你完成的任何工作将与你转移。
                            </p>
                          </div>
                        </li>
                        <li class="item">
                          <span class="on-off"
                            ><i class="i-chevron-bot"></i>
                            退款条例是如何规定的？</span
                          >
                          <div class="drop-down">
                            <p>
                              课程安排灵活，课程费用支付提供180天全程准入和资格证书。自定进度课程建议的最后期限，但你不会受到惩罚错过期限，只要你赚你的证书在180天内。以会话为基础的课程可能要求你在截止日期前保持正轨，但如果你落后了，你可以切换到以后的会议，你完成的任何工作将与你转移。
                            </p>
                          </div>
                        </li>
                        <li class="item">
                          <span class="on-off"
                            ><i class="i-chevron-bot"></i> 有助学金？</span
                          >
                          <div class="drop-down">
                            <p>
                              课程安排灵活，课程费用支付提供180天全程准入和资格证书。自定进度课程建议的最后期限，但你不会受到惩罚错过期限，只要你赚你的证书在180天内。以会话为基础的课程可能要求你在截止日期前保持正轨，但如果你落后了，你可以切换到以后的会议，你完成的任何工作将与你转移。
                            </p>
                          </div>
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
              <div class="article-right-box">
                <div class="comment">
                  <!--<div class="tit">评论</div>-->
                  <div class="com-cont">
                    <div class="item" v-for="comment in courseComments">
                      <div class="top-info">
                        <img src="http://www.xuecheng.com/cdn/img/widget-pic.png" width="30px" alt="" />
                        <div class="info">
                          <p v-text="comment.nickName"></p>
                          <p>
                            <i
                              class="i-star"
                              v-for="star in comment.starRank"
                            ></i>
                          </p>
                        </div>
                        <div
                          class="time"
                          v-text="comment.commentDate.substring(0, 10)"
                        ></div>
                      </div>
                      <div class="but-info">
                        <span v-text="comment.commentText"></span>
                      </div>
                    </div>
                    <div class="go-pingjia">
                      <span v-text="courseCommentCounts"></span>人评价
                      <a :href="goToCommentUrl">去评价</a>
                    </div>
                  </div>
                </div>
                <div class="learing-box">
                  <div
                    class="tit"
                    v-text="recentLearnedUsers.length + '人在学习该课程'"
                  ></div>
                  <div class="com-cont">
                    <a href="#" class="item" v-for="item in recentLearnedUsers"
                      ><p>
                        <img src="http://www.xuecheng.com/cdn/img/widget-pic.png" width="30px" alt="" />
                      </p>
                      <p v-text="item.name"></p
                    ></a>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="articleItem" v-show="articleTapIndex === 1">
            <div class="article-cont-catalog">
              <div class="article-left-box">
                <div class="content">
                  <#if teachplanNode?? && teachplanNode.teachPlanTreeNodes?? && (teachplanNode.teachPlanTreeNodes?size > 0)>
                  <#list teachplanNode.teachPlanTreeNodes as tl>
                  <!-- 章 -->
                  <div class="item">
                    <div class="title"><i class="i-chevron-bot"></i>${tl.pname}<span class="time">
                      <#if tl.startTime??>
                        ${tl.startTime?string('yyyy-MM-dd HH:mm:ss')}
                        </#if>
                        -
                        <#if tl.endTime??>
                        ${tl.endTime?string('yyyy-MM-dd HH:mm:ss')}
                      </#if>
                        :
                        ${tl.timelength!''}
                    </span></div>
                    <div class="about">${tl.description!''}</div>
                    <div class="drop-down">
                      <ul class="list-box">
                        <!-- 节 -->
                        <#if tl.teachPlanTreeNodes?? && (tl.teachPlanTreeNodes?size > 0)>
                        <#list tl.teachPlanTreeNodes as chapter>
                        <li>
                          <a href="http://www.xuecheng.com/pages/learing-course-video.html?coursePubId=${coursePub.id!''}&teachPlanId=${chapter.teachPlanId!''}" target="_blank">
                               ${chapter.pname}<span>${chapter.timelength!''}</span></a></li>
                        </#list>
                        </#if>
                      </ul>
                    </div>
                  </div>
                  </#list>
                  </#if>
                  <div class="item">
                    <a href="#" class="overwrite">毕业考核</a>
                  </div>
                </div>
              </div>
              <div class="article-right-box">
                <div class="about-teach">
                  <div class="teach-info">
                    <img src="http://www.xuecheng.com/cdn/img/widget-pic.png" width="90px" alt="" />
                    <div>
                      <p>教学方：<span>毛老师</span></p>
                      <p class="about">高级前端开发工程师10年开发经验</p>
                    </div>
                  </div>
                  <p><a href="#" class="">TA的课程</a></p>
                  <p class="synopsis">
                    JavaEE开发与教学多年，精通JavaEE技术体系，对流行框架JQuery、DWR、Struts1/2，Hibernate，Spring，MyBatis、JBPM、Lucene等有深入研究。授课逻辑严谨，条理清晰，注重学生独立解决问题的能力。
                  </p>
                </div>
                <div class="learing-box">
                  <div class="tit">看过该课的同学也在看</div>
                  <div class="item-box">
                    <div class="item-list hov">
                      <div class="infobox">
                        <div class="morebox">
                          <p class="top-tit">前端小白入门</p>
                          <p class="top-lab">Web前端攻城狮培养计划</p>
                          <p class="top-num">2589646次播放<span>4.8分</span></p>
                        </div>
                        <a>Linux 达人养成记</a>
                      </div>
                    </div>
                    <div class="item-list">
                      <div class="infobox">
                        <div class="morebox">
                          <p class="top-tit">前端小白入门</p>
                          <p class="top-lab">Web前端攻城狮培养计划</p>
                          <p class="top-num">2589646次播放<span>4.8分</span></p>
                        </div>
                        <a>Linux 达人养成记111</a>
                      </div>
                    </div>
                    <div class="item-list">
                      <div class="infobox">
                        <div class="morebox">
                          <p class="top-tit">前端小白入门</p>
                          <p class="top-lab">Web前端攻城狮培养计划</p>
                          <p class="top-num">2589646次播放<span>4.8分</span></p>
                        </div>
                        <a>Java消息中间件</a>
                      </div>
                    </div>
                    <div class="item-list">
                      <div class="infobox">
                        <div class="morebox">
                          <p class="top-tit">前端小白入门</p>
                          <p class="top-lab">Web前端攻城狮培养计划</p>
                          <p class="top-num">2589646次播放<span>4.8分</span></p>
                        </div>
                        <a>Hibernate Validator</a>
                      </div>
                    </div>
                    <div class="item-list">
                      <div class="infobox">
                        <div class="morebox">
                          <p class="top-tit">前端小白入门</p>
                          <p class="top-lab">Web前端攻城狮培养计划</p>
                          <p class="top-num">2589646次播放<span>4.8分</span></p>
                        </div>
                        <a>ASP.NET-MVC网站开发</a>
                      </div>
                    </div>
                    <div class="item-list">
                      <div class="infobox">
                        <div class="morebox">
                          <p class="top-tit">前端小白入门</p>
                          <p class="top-lab">Web前端攻城狮培养计划</p>
                          <p class="top-num">2589646次播放<span>4.8分</span></p>
                        </div>
                        <a>Android架构模式详解之MVC…</a>
                      </div>
                    </div>
                    <div class="item-list">
                      <div class="infobox">
                        <div class="morebox">
                          <p class="top-tit">前端小白入门</p>
                          <p class="top-lab">Web前端攻城狮培养计划</p>
                          <p class="top-num">2589646次播放<span>4.8分</span></p>
                        </div>
                        <a>Linux 达人养成记</a>
                      </div>
                    </div>
                    <div class="item-list">
                      <div class="infobox">
                        <div class="morebox">
                          <p class="top-tit">前端小白入门</p>
                          <p class="top-lab">Java消息中间件</p>
                          <p class="top-num">2589646次播放<span>4.8分</span></p>
                        </div>
                        <a>Java消息中间件</a>
                      </div>
                    </div>
                    <div class="item-list">
                      <div class="infobox">
                        <div class="morebox">
                          <p class="top-tit">前端小白入门</p>
                          <p class="top-lab">Java消息中间件</p>
                          <p class="top-num">2589646次播放<span>4.8分</span></p>
                        </div>
                        <a>Hibernate Validator</a>
                      </div>
                    </div>
                    <div class="item-list">
                      <div class="infobox">
                        <div class="morebox">
                          <p class="top-tit">前端小白入门</p>
                          <p class="top-lab">Java消息中间件</p>
                          <p class="top-num">2589646次播放<span>4.8分</span></p>
                        </div>
                        <a>ASP.NET-MVC网站开发</a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!--底部版权-->
      <footer>
        <div class="container">
          <div class="row">
            <div class="col-md-7">
              <div>
                <!--<h1 style="display: inline-block">学成网</h1>--><img
                  src="http://www.xuecheng.com/cdn/img/asset-logoIco.png"
                  alt=""
                />
              </div>
              <div>
                学成网致力于普及中国最好的教育它与中国一流大学和机构合作提供在线课程。
              </div>
              <div>© 2017年XTCG Inc.保留所有权利。-沪ICP备15025210号</div>
              <input type="button" class="btn btn-primary" value="下 载" />
            </div>
            <div class="col-md-5 row">
              <dl class="col-md-4">
                <dt>关于学成网</dt>
                <dd>关于</dd>
                <dd>管理团队</dd>
                <dd>工作机会</dd>
                <dd>客户服务</dd>
                <dd>帮助</dd>
              </dl>
              <dl class="col-md-4">
                <dt>新手指南</dt>
                <dd>如何注册</dd>
                <dd>如何选课</dd>
                <dd>如何拿到毕业证</dd>
                <dd>学分是什么</dd>
                <dd>考试未通过怎么办</dd>
              </dl>
              <dl class="col-md-4">
                <dt>合作伙伴</dt>
                <dd>合作机构</dd>
                <dd>合作导师</dd>
              </dl>
            </div>
          </div>
        </div>
      </footer>
    </div>
    <!-- 页面 css js -->

    <script
      type="text/javascript"
      src="http://www.xuecheng.com/cdn/plugins/jquery/dist/jquery.js"
    ></script>
    <script
      type="text/javascript"
      src="http://www.xuecheng.com/cdn/plugins/bootstrap/dist/js/bootstrap.js"
    ></script>
    <script
      type="text/javascript"
      src="http://www.xuecheng.com/cdn/plugins/jscookie/js.cookie.js"
    ></script>
    <script type="text/javascript" src="http://www.xuecheng.com/cdn/plugins/vue/vue.js"></script>
    <script type="text/javascript" src="http://www.xuecheng.com/cdn/js/util-env.js"></script>
    <script type="text/javascript" src="http://www.xuecheng.com/cdn/js/util-request-ajax.js"></script>
    <script type="text/javascript" src="http://www.xuecheng.com/cdn/js/util-api.js"></script>
    <script type="text/javascript" src="http://www.xuecheng.com/cdn/js/widget-header.js"></script>
    <script type="text/javascript" src="http://www.xuecheng.com/cdn/js/widget-header_vue.js"></script>
    <script
      type="text/javascript"
      src="http://www.xuecheng.com/cdn/js/widget-learing-article-banner.js"
    ></script>
    <script
      type="text/javascript"
      src="http://www.xuecheng.com/cdn/js/widget-learing-article-catalog-cont.js"
    ></script>
    <script type="text/javascript" src="http://www.xuecheng.com/cdn/js/page-learing-article.js"></script>
  </body>
</html>
