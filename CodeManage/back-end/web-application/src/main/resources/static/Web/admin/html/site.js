//
// site.js
//
// the arbor.js website
//
(function ($) {
  // var trace = function(msg){
  //   if (typeof(window)=='undefined' || !window.console) return
  //   var len = arguments.length, args = [];
  //   for (var i=0; i<len; i++) args.push("arguments["+i+"]")
  //   eval("console.log("+args.join(",")+")")
  // }  
  var intersect_line_line = function (p1, p2, p3, p4) {
    var denom = ((p4.y - p3.y) * (p2.x - p1.x) - (p4.x - p3.x) * (p2.y - p1.y));
    if (denom === 0) return false // lines are parallel
    var ua = ((p4.x - p3.x) * (p1.y - p3.y) - (p4.y - p3.y) * (p1.x - p3.x)) / denom;
    var ub = ((p2.x - p1.x) * (p1.y - p3.y) - (p2.y - p1.y) * (p1.x - p3.x)) / denom;

    if (ua < 0 || ua > 1 || ub < 0 || ub > 1) return false
    return arbor.Point(p1.x + ua * (p2.x - p1.x), p1.y + ua * (p2.y - p1.y));
  }

  var intersect_line_box = function (p1, p2, boxTuple) {
    var p3 = {
        x: boxTuple[0],
        y: boxTuple[1]
      },
      w = boxTuple[2],
      h = boxTuple[3]

    var tl = {
      x: p3.x,
      y: p3.y
    };
    var tr = {
      x: p3.x + w,
      y: p3.y
    };
    var bl = {
      x: p3.x,
      y: p3.y + h
    };
    var br = {
      x: p3.x + w,
      y: p3.y + h
    };

    return intersect_line_line(p1, p2, tl, tr) ||
      intersect_line_line(p1, p2, tr, br) ||
      intersect_line_line(p1, p2, br, bl) ||
      intersect_line_line(p1, p2, bl, tl) ||
      false
  }
  var Renderer = function (elt) {
    var dom = $(elt)
    var canvas = dom.get(0)
    var ctx = canvas.getContext("2d");
    var gfx = arbor.Graphics(canvas)
    var sys = null

    var _vignette = null
    var selected = null,
      nearest = null,
      _mouseP = null;


    var that = {
      init: function (pSystem) {
        sys = pSystem
        sys.screen({
          size: {
            width: dom.width(),
            height: dom.height()
          },
          padding: [36, 60, 36, 60]
        })

        $(window).resize(that.resize)
        that.resize()
        that._initMouseHandling()

        if (document.referrer.match(/echolalia|atlas|halfviz/)) {
          // if we got here by hitting the back button in one of the demos, 
          // start with the demos section pre-selected
          that.switchSection('demos')
        }
      },
      resize: function () {
        canvas.width = $(window).width()
        canvas.height = .75 * $(window).height()
        sys.screen({
          size: {
            width: canvas.width,
            height: canvas.height
          }
        })
        _vignette = null
        that.redraw()
      },

      intersect_line_line: function (p1, p2, p3, p4) {
        var denom = ((p4.y - p3.y) * (p2.x - p1.x) - (p4.x - p3.x) * (p2.y - p1.y));
        if (denom === 0) return false // lines are parallel
        var ua = ((p4.x - p3.x) * (p1.y - p3.y) - (p4.y - p3.y) * (p1.x - p3.x)) / denom;
        var ub = ((p2.x - p1.x) * (p1.y - p3.y) - (p2.y - p1.y) * (p1.x - p3.x)) / denom;

        if (ua < 0 || ua > 1 || ub < 0 || ub > 1) return false
        return arbor.Point(p1.x + ua * (p2.x - p1.x), p1.y + ua * (p2.y - p1.y));
      },
      redraw: function () {
        var nodeBoxes = {}
        gfx.clear();
        sys.eachNode(function (node, pt) {
          var w = Math.max(20, 20 + gfx.textWidth(node.name))
          if (node.data.alpha === 0) return
          if (node.data.shape == 'dot') {
            nodeBoxes[node.name] = [pt.x - w / 2, pt.y - w / 2, w, w]
          } else {
            nodeBoxes[node.name] = [pt.x - w / 2, pt.y - 11, w, 22]
          }
        })

        sys.eachEdge(function (edge, p1, p2) {
          if (edge.source.data.alpha * edge.target.data.alpha == 0)
            return
          //gfx.line(p1, p2, {stroke:"#b2b19d", width:2, alpha:edge.target.data.alpha})
          //ctx.fillStyle = "black";
          //ctx.font = 'italic 13px sans-serif';
          //ctx.fillText (edge.data.label, (p1.x + p2.x) / 2, (p1.y + p2.y) / 2); 

          var weight = edge.data.weight
          var color = edge.data.color

          if (!color || ("" + color).match(/^[ \t]*$/)) color = null

          // find the start point
          var tail = intersect_line_box(p1, p2, nodeBoxes[edge.source.name])
          var head = intersect_line_box(tail, p2, nodeBoxes[edge.target.name])
          //head=p2;
          //tail=p1;
          //*
          ctx.save()
          ctx.beginPath()
          ctx.lineWidth = (!isNaN(weight)) ? parseFloat(weight) : 1
          ctx.strokeStyle = (color) ? color : "#cccccc"
          ctx.fillStyle = "null"
          ctx.moveTo(p1.x, p1.y)
          ctx.lineTo(p2.x, p2.y)
          ctx.closePath()
          ctx.stroke()
          ctx.restore()
          //*/
          // draw an arrowhead if this is a -> style edge
          if (edge.data.directed) {
            ctx.save()
            // move to the head position of the edge we just drew
            var wt = !isNaN(weight) ? parseFloat(weight) : 1
            var arrowLength = 6 + wt
            var arrowWidth = 2 + wt
            ctx.fillStyle = (color) ? color : "#cccccc"
            ctx.translate(head.x, head.y);
            ctx.rotate(Math.atan2(head.y - tail.y, head.x - tail.x));

            // delete some of the edge that's already there (so the point isn't hidden)
            ctx.clearRect(-arrowLength / 2, -wt / 2, arrowLength / 2, wt)

            // draw the chevron
            ctx.beginPath();
            ctx.moveTo(-arrowLength, arrowWidth);
            ctx.lineTo(0, 0);
            ctx.lineTo(-arrowLength, -arrowWidth);
            ctx.lineTo(-arrowLength * 0.8, -0);
            ctx.closePath();
            ctx.fill();
            ctx.restore()
          }
          //*/
        })
        sys.eachNode(function (node, pt) {
          var w = Math.max(20, 20 + gfx.textWidth(node.name))
          if (node.data.alpha === 0) return
          if (node.data.shape == 'dot') {
            gfx.oval(pt.x - w / 2, pt.y - w / 2, w, w, {
              fill: node.data.color,
              alpha: node.data.alpha
            })
            gfx.text(node.data.label, pt.x, pt.y + 7, {
              color: "white",
              align: "center",
              font: "Arial",
              size: 12
            })
            gfx.text(node.data.label, pt.x, pt.y + 7, {
              color: "white",
              align: "center",
              font: "Arial",
              size: 12
            })
          } else {
            gfx.rect(pt.x - w / 2, pt.y - 8, w, 20, 4, {
              fill: node.data.color,
              alpha: node.data.alpha
            })
            gfx.text(node.data.label, pt.x, pt.y + 9, {
              color: "white",
              align: "center",
              font: "Arial",
              size: 12
            })
            gfx.text(node.data.label, pt.x, pt.y + 9, {
              color: "white",
              align: "center",
              font: "Arial",
              size: 12
            })
          }
        })
        that._drawVignette()
      },

      _drawVignette: function () {
        var w = canvas.width
        var h = canvas.height
        var r = 20

        if (!_vignette) {
          var top = ctx.createLinearGradient(0, 0, 0, r)
          top.addColorStop(0, "#e0e0e0")
          top.addColorStop(.7, "rgba(255,255,255,0)")

          var bot = ctx.createLinearGradient(0, h - r, 0, h)
          bot.addColorStop(0, "rgba(255,255,255,0)")
          bot.addColorStop(1, "white")

          _vignette = {
            top: top,
            bot: bot
          }
        }

        // top
        ctx.fillStyle = _vignette.top
        ctx.fillRect(0, 0, w, r)

        // bot
        ctx.fillStyle = _vignette.bot
        ctx.fillRect(0, h - r, w, r)
      },

      switchMode: function (e) {
        if (e.mode == 'hidden') {
          dom.stop(true).fadeTo(e.dt, 0, function () {
            if (sys) sys.stop()
            $(this).hide()
          })
        } else if (e.mode == 'visible') {
          dom.stop(true).css('opacity', 0).show().fadeTo(e.dt, 1, function () {
            that.resize()
          })
          if (sys) sys.start()
        }
      },

      switchSection: function (newSection) {
        var parent = sys.getEdgesFrom(newSection)[0].source
        var children = $.map(sys.getEdgesFrom(newSection), function (edge) {
          return edge.target
        })

        sys.eachNode(function (node) {
          if (node.data.shape == 'dot') return // skip all but leafnodes

          var nowVisible = ($.inArray(node, children) >= 0)
          var newAlpha = (nowVisible) ? 1 : 0
          var dt = (nowVisible) ? .5 : .5
          sys.tweenNode(node, dt, {
            alpha: newAlpha
          })

          if (newAlpha == 1) {
            node.p.x = parent.p.x + .05 * Math.random() - .025
            node.p.y = parent.p.y + .05 * Math.random() - .025
            node.tempMass = .001
          }
        })
      },


      _initMouseHandling: function () {
        // no-nonsense drag and drop (thanks springy.js)
        selected = null;
        nearest = null;
        var ppoint = null;
        var dragged = null;
        var oldmass = 1
        var _section = null

        var handler = {
          moved: function (e) {
            var pos = $(canvas).offset();
            _mouseP = arbor.Point(e.pageX - pos.left, e.pageY - pos.top)
            nearest = sys.nearest(_mouseP);
            if (!nearest.node) return false;
            return false
          },
          clicked: function (e) {
            selected = (nearest.distance < 20) ? nearest : null
            if (selected === null) {
              return false;
            }
            var pos = $(canvas).offset();
            _mouseP = arbor.Point(e.pageX - pos.left, e.pageY - pos.top)
            ppoint = _mouseP;
            nearest = dragged = sys.nearest(_mouseP);
            if (dragged && dragged.node !== null) dragged.node.fixed = true

            $(canvas).unbind('mousemove', handler.moved);
            $(canvas).bind('mousemove', handler.dragged)
            $(window).bind('mouseup', handler.dropped)

            return false
          },
          dragged: function (e) {
            var old_nearest = nearest && nearest.node._id
            var pos = $(canvas).offset();
            var s = arbor.Point(e.pageX - pos.left, e.pageY - pos.top)

            if (!nearest) return
            if (dragged !== null && dragged.node !== null) {
              var p = sys.fromScreen(s)
              dragged.node.p = p
            }
            return false
          },

          dropped: function (e) {
            if (dragged === null || dragged.node === undefined) {
              return false;
            }
            if (dragged.node !== null) dragged.node.fixed = false
            dragged.node.tempMass = 1000
            var popup; //
            var pos = $(canvas).offset();
            var s = arbor.Point(e.pageX - pos.left, e.pageY - pos.top);
            moveleft = ppoint.x - s.x;
            moveright = ppoint.y - s.y;
            if (Math.abs(moveleft) < 5 && nearest.node.name != _section && nearest.node === selected.node) {
              // _section = nearest.node.name
              // that.switchSection(_section)
              //alert($(".float_combox").html());
              //  $(".float_combox").attr("style","position:absolute;top:"+e.pageY+"px;left:"+e.pageX+"px");
              //alert(selected.node.label)
              // var cell = document.getElementById("relstable").rows[0].cells;
              // cell[0].innerHTML = "<input readonly='true' id='graphid' value='udh'/>"
              // $(".float_combox").attr("style", "position:absolute;top:" + e.pageY + "px;left:" + e.pageX + "px");
            }

            $(canvas).unbind('mousemove', handler.dragged)
            $(window).unbind('mouseup', handler.dropped)
            $(canvas).bind('mousemove', handler.moved);
            dragged = null;
            _mouseP = null;
            selected = null
            return false
          }
        }
        $(canvas).mousedown(handler.clicked);
        $(canvas).mousemove(handler.moved);
      }
    }
    return that
  }


  var Nav = function (elt) {
    var dom = $(elt)

    var _path = null

    var that = {
      init: function () {
        $(window).bind('popstate', that.navigate)
        dom.find('> a').click(that.back)
        $('.more').one('click', that.more)

        $('#docs dl:not(.datastructure) dt').click(that.reveal)
        that.update()
        return that
      },
      more: function (e) {
        $(this).removeAttr('href').addClass('less').html('&nbsp;').siblings().fadeIn()
        $(this).next('h2').find('a').one('click', that.less)

        return false
      },
      less: function (e) {
        var more = $(this).closest('h2').prev('a')
        $(this).closest('h2').prev('a')
          .nextAll().fadeOut(function () {
            $(more).text('creation & use').removeClass('less').attr('href', '#')
          })
        $(this).closest('h2').prev('a').one('click', that.more)

        return false
      },
      reveal: function (e) {
        $(this).next('dd').fadeToggle('fast')
        return false
      },
      back: function () {
        _path = "/"
        if (window.history && window.history.pushState) {
          window.history.pushState({
            path: _path
          }, "", _path);
        }
        that.update()
        return false
      },
      navigate: function (e) {
        var oldpath = _path
        if (e.type == 'navigate') {
          _path = e.path
          if (window.history && window.history.pushState) {
            window.history.pushState({
              path: _path
            }, "", _path);
          } else {
            that.update()
          }
        } else if (e.type == 'popstate') {
          var state = e.originalEvent.state || {}
          _path = state.path || window.location.pathname.replace(/^\//, '')
        }
        if (_path != oldpath) that.update()
      },
      update: function () {
        var dt = 'fast'
        if (_path === null) {
          // this is the original page load. don't animate anything just jump
          // to the proper state
          _path = window.location.pathname.replace(/^\//, '')
          dt = 0
          dom.find('p').css('opacity', 0).show().fadeTo('slow', 1)
        }

        switch (_path) {
          case '':
          case '/':
            dom.find('p').text('a graph visualization library using controller workers and jQuery')
            dom.find('> a').removeClass('active').attr('href', '#')

            $('#docs').fadeTo('fast', 0, function () {
              $(this).hide()
              $(that).trigger({
                type: 'mode',
                mode: 'visible',
                dt: dt
              })
            })
            document.title = "arbor.js"
            break

          case 'introduction':
          case 'reference':
            $(that).trigger({
              type: 'mode',
              mode: 'hidden',
              dt: dt
            })
            dom.find('> p').text(_path)
            dom.find('> a').addClass('active').attr('href', '#')
            $('#docs').stop(true).css({
              opacity: 0
            }).show().delay(333).fadeTo('fast', 1)

            $('#docs').find(">div").hide()
            $('#docs').find('#' + _path).show()
            document.title = "arbor.js » " + _path
            break
        }

      }
    }
    return that
  }
  $(document).ready(function () {
    var LEV = {
      lev1: "#FFD2D2",
      lev2: "#FF9797",
      lev3: "#FF5151",
      lev4: "#FF0000",
      lev5: "#CE0000",
      lev6: "#930000",
      lev7: "#600000",
      lev8: "#6F0000"
    }

    var gdsFig = {
      nodes: {},
      edges: {}
    }

    var sys = arbor.ParticleSystem();
    sys.parameters({
      stiffness: 900,
      repulsion: 2000,
      gravity: true,
      dt: 0.015,
      precision: 0.8
    });
    sys.renderer = Renderer("#sitemap");
    sys.graft(gdsFig);

    var nav = Nav("#nav");
    $(sys.renderer).bind('navigate', nav.navigate);
    $(nav).bind('mode', sys.renderer.switchMode);
    nav.init();

    constructGdsFig();

    function addGdsNode(gds, cnt, num) {
      if (num < -0.75) {
        sys.addNode(gds, {
          label: '' + gds + '(' + cnt + ')',
          color: LEV.lev1,
          shape: "bar",
          alpha: 1
        });
      } else if (num < -0.5) {
        sys.addNode(gds, {
          label: '' + gds + '(' + cnt + ')',
          color: LEV.lev2,
          shape: "bar",
          alpha: 1
        });
      } else if (num < -0.25) {
        sys.addNode(gds, {
          label: '' + gds + '(' + cnt + ')',
          color: LEV.lev3,
          shape: "bar",
          alpha: 1
        });
      } else if (num < 0) {
        sys.addNode(gds, {
          label: '' + gds + '(' + cnt + ')',
          color: LEV.lev4,
          shape: "bar",
          alpha: 1
        });
      } else if (num < 0.25) {
        sys.addNode(gds, {
          label: '' + gds + '(' + cnt + ')',
          color: LEV.lev5,
          shape: "bar",
          alpha: 1
        });
      } else if (num < 0.5) {
        sys.addNode(gds, {
          label: '' + gds + '(' + cnt + ')',
          color: LEV.lev6,
          shape: "bar",
          alpha: 1
        });
      } else if (num < 0.75) {
        sys.addNode(gds, {
          label: '' + gds + '(' + cnt + ')',
          color: LEV.lev7,
          shape: "bar",
          alpha: 1
        });
      } else {
        sys.addNode(gds, {
          label: '' + gds + '(' + cnt + ')',
          color: LEV.lev8,
          shape: "bar",
          alpha: 1
        });
      }

    }

    function addGdsEdge(gds1, gds2, num) {
      if (num < 4.5) {
        sys.addEdge(gds1, gds2, {
          // label: '2ww',
          color: LEV.lev1
        });
      } else if (num < 5) {
        sys.addEdge(gds1, gds2, {
          // label: '2ww',
          color: LEV.lev2
        });
      } else if (num < 6) {
        sys.addEdge(gds1, gds2, {
          // label: '2ww',
          color: LEV.lev3
        });
      } else if (num < 8) {
        sys.addEdge(gds1, gds2, {
          // label: '2ww',
          color: LEV.lev4
        });
      } else if (num < 10) {
        sys.addEdge(gds1, gds2, {
          // label: '2ww',
          color: LEV.lev5
        });
      } else if (num < 15) {
        sys.addEdge(gds1, gds2, {
          // label: '2ww',
          color: LEV.lev6
        });
      } else if (num < 20) {
        sys.addEdge(gds1, gds2, {
          // label: '2ww',
          color: LEV.lev7
        });
      } else {
        sys.addEdge(gds1, gds2, {
          // label: '2ww',
          color: LEV.lev8
        });
      }
    }

    $('#sale-gds-cnt-table').bootstrapTable({
      // data: vals,
      /**
       * gds:
       *      id
       *      name
       *      type
       *      prc
       *      cnt
       *      buy_cnt
       */
      columns: [{
        field: 'gds_id',
        title: '商品ID',
        sortable: true
      }, {
        field: 'gds_name',
        title: '商品名称'
      }, {
        field: 'gds_type',
        title: '商品类型'
      }, {
        field: 'gds_prc',
        title: '商品价格'
      },{
        field: 'gds_cnt',
        title: '库存数量',
        sortable: true
      },{
        field: 'buy_cnt',
        title: '商品销量',
        sortable: true
      }],
      // toolbar: '#toolbar',
      cache: true,
      striped: true,
      pagination: true,                   //是否显示分页（*）
      sortable: true,                     //是否启用排序
      sortOrder: 'des',
      search: true,
      strictSearch: false,
      showRefresh: true,
      sidePagination: "client",
      pageSize: 5,
      onLoadSuccess: function () {
      },
      onLoadError: function () {
        showTips("数据加载失败！");
      }
    });

    var GdsTableInit = function(vals){
      var oTableInit = new Object();
      oTableInit.init=function(){
        $('#sale-gds-cnt-table').bootstrapTable('load', vals);
      };
      return oTableInit;
    }

    function presentGds(vals) {
      // alert(vals[0]);
      var oTable = GdsTableInit(vals);
      oTable.init();
    }


    function pullGdsHeatFig() {
      alert("post heat start");

      $.post("goodsAnalPull", {
        mngrid:"default"
      }, function (result) {
        alert(result);
        /**
         * obj
         *    stat:success
         *    gds_lst:[
         *      {
         *          gds_name:
         *          gds_id:
         *          buy_cnt:
         *          cnt_num:
         *      }
         *    ],
         *    rel_lst:[{
         *        gds1_name:
         *        gds1_id:
         *        gds2_name:
         *        gds2_id:
         *        rel_num:
         *    }]
         *
         */

        var obj = JSON.parse(result);
        if (obj.stat != "success") {
          alert('商品关联数据获取失败。');
          return;
        } else {
          // alert('obj:\t'+obj);
          // alert('obj.gds_lst:\t'+obj.gds_lst);
          // alert('obj.rel_lst:\t'+obj.rel_lst);
          // alert('obj.gds_lst[0]:\t'+obj.gds_lst[0]);
          // alert('obj.rel_lst[0]:\t'+obj.rel_lst[0]);
          // alert('obj.gds_lst[0].gds_name:\t'+obj.gds_lst[0].gds_name);
          // alert('obj.gds_lst[0].gds_id:\t'+obj.gds_lst[0].gds_id);
          // alert('obj.gds_lst[0].buy_cnt:\t'+obj.gds_lst[0].buy_cnt);
          // alert('obj.gds_lst[0].cnt_num:\t'+obj.gds_lst[0].cnt_num);
          // alert('obj.rel_lst[0].gds1_name:\t'+obj.rel_lst[0].gds1_name);
          // alert('obj.rel_lst[0].gds1_id:\t'+obj.rel_lst[0].gds1_id);
          // alert('obj.rel_lst[0].gds2_name:\t'+obj.rel_lst[0].gds2_name);
          // alert('obj.rel_lst[0].gds2_id:\t'+obj.rel_lst[0].gds2_id);
          // alert('obj.rel_lst[0].rel_num:\t'+obj.rel_lst[0].rel_num);

          for (var i = 0; i<obj.gds_lst.length;++i)
          {
            addGdsNode(obj.gds_lst[i].gds_name,obj.gds_lst[i].buy_cnt,obj.gds_lst[i].cnt_num);
            // alert("gds:"+obj.gds_lst[i].gds_id);
          }

          for (var i = 0; i<obj.rel_lst.length;++i)
          {
            addGdsEdge(obj.rel_lst[i].gds1_name,obj.rel_lst[i].gds2_name,obj.rel_lst[i].rel_num);
          }

          alert('gds_type:\t'+obj.gds_lst[0].gds_type);
          alert('gds_prc:\t'+obj.gds_lst[0].gds_prc);
          alert('gds_name:\t'+obj.gds_lst[0].gds_name);
          alert('gds_cnt:\t'+obj.gds_lst[0].gds_cnt);
          alert('buy_cnt:\t'+obj.gds_lst[0].buy_cnt);
          var lst = [];
          for(var i = 0; i < obj.gds_lst.length;++i)
          {
            lst.push({
                'gds_id':obj.gds_lst[i].gds_id,
              'gds_name':obj.gds_lst[i].gds_name,
              'gds_type':obj.gds_lst[i].gds_type,
              'gds_prc':obj.gds_lst[i].gds_prc,
              'gds_cnt':obj.gds_lst[i].gds_cnt,
              'buy_cnt':obj.gds_lst[i].buy_cnt
            });
          }
          alert('lst'+lst);
          presentGds(lst);
        }
      });
      alert("post heat end.");
    }

    function constructGdsFig() {

      // var max = 30;
      //
      // for (var i = 0; i < max; ++i) {
      //   var rd = Math.random() * 2 - 1;
      //   addGdsNode('why' + i, i, rd);
      // }
      //
      // // addGdsEdge('why1', 'why2', 0.5);
      //
      // for (var i = 0; i < max; ++i) {
      //   var rd = Math.random() * 2 - 1;
      //   var gds1 = parseInt(Math.random() * (max - 1), 10) + 1;
      //   var gds2 = parseInt(Math.random() * (max - 1), 10) + 1;
      //   addGdsEdge('why' + gds1, 'why' + gds2, rd);
      // }

      pullGdsHeatFig();
      // addGdsNode('why2', 13);
      // addGdsEdge('why1', 'why2', 100);
    }

  })
})(this.jQuery)



// var goodsFig = {
//   nodes: {
//     "why1": {
//       label: 'why11',
//       color: "red",
//       shape: "dot",
//       alpha: 1
//     },
//     "why2": {
//       label: 'why22',
//       color: "red",
//       shape: "dot",
//       alpha: 1
//     },
//     "why3": {
//       label: 'why33',
//       color: "red",
//       shape: "dot",
//       alpha: 1
//     }
//   },
//   edges: {
//     "why1": {
//       "why2": {
//         label: 'jj',
//         length: .8,
//         directed: true
//       },
//       "why3": {
//         label: 'jj',
//         length: .8,
//         directed: true
//       }
//     },
//     "why2": {
//       "why3": {
//         label: 'jj',
//         length: .8,
//         directed: true
//       }
//     }
//   }
// }

// var theUI = {
//   nodes: {
//     "arbor.js": {
//       label: 'arbor',
//       color: "red",
//       shape: "dot",
//       alpha: 1
//     },

//     demos: {
//       label: 'demo',
//       color: CLR.branch,
//       shape: "dot",
//       alpha: 1
//     },
//     halfviz: {
//       color: CLR.demo,
//       alpha: 0,
//       link: '/halfviz'
//     },
//     atlas: {
//       color: CLR.demo,
//       alpha: 0,
//       link: '/atlas'
//     },
//     echolalia: {
//       color: CLR.demo,
//       alpha: 0,
//       link: '/echolalia'
//     },

//     docs: {
//       label: 'doc',
//       color: CLR.branch,
//       shape: "dot",
//       alpha: 1
//     },
//     reference: {
//       color: CLR.doc,
//       alpha: 0,
//       link: '#reference'
//     },
//     introduction: {
//       color: CLR.doc,
//       alpha: 0,
//       link: '#introduction'
//     },

//     code: {
//       label: 'code',
//       color: CLR.branch,
//       shape: "dot",
//       alpha: 1
//     },
//     github: {
//       color: CLR.code,
//       alpha: 0,
//       link: 'https://github.com/samizdatco/arbor'
//     },
//     ".zip": {
//       color: CLR.code,
//       alpha: 0,
//       link: '/js/dist/arbor-v0.92.zip'
//     },
//     ".tar.gz": {
//       color: CLR.code,
//       alpha: 0,
//       link: '/js/dist/arbor-v0.92.tar.gz'
//     }
//   },
//   edges: {
//     "arbor.js": {
//       demos: {
//         label: 'jj',
//         length: .8,
//         directed: true
//       },
//       docs: {
//         label: 'doc',
//         length: .8
//       },
//       code: {
//         label: 'code',
//         length: .8
//       }
//     },
//     demos: {
//       halfviz: {},
//       atlas: {},
//       echolalia: {}
//     },
//     docs: {
//       reference: {},
//       introduction: {}
//     },
//     code: {
//       ".zip": {},
//       ".tar.gz": {},
//       "github": {}
//     }
//   }
// }

// $("Button").click(function () {
//   //alert($(".float_combox").html());
//   //alert("nihao");
//   var nn = sys.addNode("FSB", {
//     label: 'nihao',
//     color: CLR.branch,
//     shape: "dot",
//     alpha: 1,
//     expanded: true
//   });
//   var nm = sys.addNode("FSB1", {
//     label: 'sad',
//     color: CLR.branch,
//     shape: "dot",
//     alpha: 1
//   });
//   var ee = sys.addEdge(nm, nn, {
//     label: "nihao"
//   });
//   var nodes = sys.getEdges(nm, nn);
//   nodes[0].data.label = "nihaoasfsa";
//   alert("" + nodes[0].data.label);
// });