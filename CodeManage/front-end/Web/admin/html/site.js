//
// site.js
//
// the arbor.js website
//
(function ($) {
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

      redraw: function () {

      },


      switchMode: function (e) {

      },

      switchSection: function (newSection) {

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
            return false
          },
          clicked: function (e) {

            return false
          },
          dragged: function (e) {
            return false
          },

          dropped: function (e) {
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
            dom.find('p').text('a graph visualization library using web workers and jQuery')
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
          shape: "dot",
          alpha: 1
        });
      } else if (num < -0.5) {
        sys.addNode(gds, {
          label: '' + gds + '(' + cnt + ')',
          color: LEV.lev2,
          shape: "dot",
          alpha: 1
        });
      } else if (num < -0.25) {
        sys.addNode(gds, {
          label: '' + gds + '(' + cnt + ')',
          color: LEV.lev3,
          shape: "dot",
          alpha: 1
        });
      } else if (num < 0) {
        sys.addNode(gds, {
          label: '' + gds + '(' + cnt + ')',
          color: LEV.lev4,
          shape: "dot",
          alpha: 1
        });
      } else if (num < 0.25) {
        sys.addNode(gds, {
          label: '' + gds + '(' + cnt + ')',
          color: LEV.lev5,
          shape: "dot",
          alpha: 1
        });
      } else if (num < 0.5) {
        sys.addNode(gds, {
          label: '' + gds + '(' + cnt + ')',
          color: LEV.lev6,
          shape: "dot",
          alpha: 1
        });
      } else if (num < 0.75) {
        sys.addNode(gds, {
          label: '' + gds + '(' + cnt + ')',
          color: LEV.lev7,
          shape: "dot",
          alpha: 1
        });
      } else {
        sys.addNode(gds, {
          label: '' + gds + '(' + cnt + ')',
          color: LEV.lev8,
          shape: "dot",
          alpha: 1
        });
      }

    }

    function addGdsEdge(gds1, gds2, num) {
      if (num < -0.75) {
        sys.addEdge(gds1, gds2, {
          // label: '2ww',
          color: LEV.lev1
        });
      } else if (num < -0.5) {
        sys.addEdge(gds1, gds2, {
          // label: '2ww',
          color: LEV.lev2
        });
      } else if (num < -0.25) {
        sys.addEdge(gds1, gds2, {
          // label: '2ww',
          color: LEV.lev3
        });
      } else if (num < 0) {
        sys.addEdge(gds1, gds2, {
          // label: '2ww',
          color: LEV.lev4
        });
      } else if (num < 0.25) {
        sys.addEdge(gds1, gds2, {
          // label: '2ww',
          color: LEV.lev5
        });
      } else if (num < 0.5) {
        sys.addEdge(gds1, gds2, {
          // label: '2ww',
          color: LEV.lev6
        });
      } else if (num < 0.75) {
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

    function constructGdsFig() {

      var max = 30;

      for (var i = 0; i < max; ++i) {
        var rd = Math.random() * 2 - 1;
        addGdsNode('why' + i, i, rd);
      }

      // addGdsEdge('why1', 'why2', 0.5);

      for (var i = 0; i < max; ++i) {
        var rd = Math.random() * 2 - 1;
        var gds1 = parseInt(Math.random() * (max - 1), 10) + 1;
        var gds2 = parseInt(Math.random() * (max - 1), 10) + 1;
        addGdsEdge('why' + gds1, 'why' + gds2, rd);
      }

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