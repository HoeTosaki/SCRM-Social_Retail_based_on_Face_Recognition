//
//  main.js
//
//  A project template for using arbor.js
//

(function ($) {

  var Renderer = function (canvas) {
    var canvas = $(canvas).get(0)
    var ctx = canvas.getContext("2d");
    var particleSystem

    var that = {
      init: function (system) {
        //
        // the particle system will call the init function once, right before the
        // first frame is to be drawn. it's a good place to set up the canvas and
        // to pass the canvas size to the particle system
        //
        // save a reference to the particle system for use in the .redraw() loop
        particleSystem = system

        // inform the system of the screen dimensions so it can map coords for us.
        // if the canvas is ever resized, screenSize should be called again with
        // the new dimensions
        particleSystem.screenSize(canvas.width, canvas.height)
        particleSystem.screenPadding(80) // leave an extra 80px of whitespace per side

        // set up some event handlers to allow for node-dragging
        that.initMouseHandling()
      },

      redraw: function () {
        // 
        // redraw will be called repeatedly during the run whenever the node positions
        // change. the new positions for the nodes can be accessed by looking at the
        // .p attribute of a given node. however the p.x & p.y values are in the coordinates
        // of the particle system rather than the screen. you can either map them to
        // the screen yourself, or use the convenience iterators .eachNode (and .eachEdge)
        // which allow you to step through the actual node objects but also pass an
        // x,y point in the screen's coordinate system
        // 
        ctx.fillStyle = "white"
        ctx.fillRect(0, 0, canvas.width, canvas.height)

        particleSystem.eachEdge(function (edge, pt1, pt2) {
          // edge: {source:Node, target:Node, length:#, data:{}}
          // pt1:  {x:#, y:#}  source position in screen coords
          // pt2:  {x:#, y:#}  target position in screen coords

          // draw a line from pt1 to pt2
          ctx.strokeStyle = "rgba(0,0,0, .333)"
          ctx.lineWidth = 1
          ctx.beginPath()
          ctx.moveTo(pt1.x, pt1.y)
          ctx.lineTo(pt2.x, pt2.y)
          ctx.stroke()
        })

        particleSystem.eachNode(function (node, pt) {
          // node: {mass:#, p:{x,y}, name:"", data:{}}
          // pt:   {x:#, y:#}  node position in screen coords

          // draw a rectangle centered at pt
          var w = 10
          ctx.fillStyle = (node.data.alone) ? "orange" : "black"
          ctx.fillRect(pt.x - w / 2, pt.y - w / 2, w, w)
        })
      },

      initMouseHandling: function () {
        // no-nonsense drag and drop (thanks springy.js)
        var dragged = null;

        // set up a handler object that will initially listen for mousedowns then
        // for moves and mouseups while dragging
        var handler = {
          clicked: function (e) {
            var pos = $(canvas).offset();
            _mouseP = arbor.Point(e.pageX - pos.left, e.pageY - pos.top)
            dragged = particleSystem.nearest(_mouseP);

            if (dragged && dragged.node !== null) {
              // while we're dragging, don't let physics move the node
              dragged.node.fixed = true
            }

            $(canvas).bind('mousemove', handler.dragged)
            $(window).bind('mouseup', handler.dropped)

            return false
          },
          dragged: function (e) {
            var pos = $(canvas).offset();
            var s = arbor.Point(e.pageX - pos.left, e.pageY - pos.top)

            if (dragged && dragged.node !== null) {
              var p = particleSystem.fromScreen(s)
              dragged.node.p = p
            }

            return false
          },

          dropped: function (e) {
            if (dragged === null || dragged.node === undefined) return
            if (dragged.node !== null) dragged.node.fixed = false
            dragged.node.tempMass = 1000
            dragged = null
            $(canvas).unbind('mousemove', handler.dragged)
            $(window).unbind('mouseup', handler.dropped)
            _mouseP = null
            return false
          }
        }

        // start listening
        $(canvas).mousedown(handler.clicked);

      },

    }
    return that
  }

  function addGdsNode(gds, num) {
    sys.addNode(gds, {
      alone: true,
      mass: .25
    });
  }

  function addGdsRel(sys, gds1, gds2, relNum) {
    sys.addEdge(gds1, gds2);
  }

  $(document).ready(function () {
    var sys = arbor.ParticleSystem(1000, 600, 0.5) // create the system with sensible repulsion/stiffness/friction

    sys.parameters({
      stiffness: 900,
      repulsion: 2000,
      gravity: true,
      dt: 0.015,
      precision: 0.8
    })

    sys.renderer = Renderer("#viewport") // our newly created renderer will have its .init() method called shortly by sys...


    var CLR = {
      branch: "#b2b19d",
      code: "orange",
      doc: "#922E00",
      demo: "#a7af00"
    }

    var theUI = {
      nodes: {
        "arbor.js": {
          label: 'arbor',
          color: "red",
          shape: "dot",
          alpha: 1
        },

        demos: {
          label: 'demo',
          color: CLR.branch,
          shape: "dot",
          alpha: 1
        },
        halfviz: {
          color: CLR.demo,
          alpha: 0,
          link: '/halfviz'
        },
        atlas: {
          color: CLR.demo,
          alpha: 0,
          link: '/atlas'
        },
        echolalia: {
          color: CLR.demo,
          alpha: 0,
          link: '/echolalia'
        },

        docs: {
          label: 'doc',
          color: CLR.branch,
          shape: "dot",
          alpha: 1
        },
        reference: {
          color: CLR.doc,
          alpha: 0,
          link: '#reference'
        },
        introduction: {
          color: CLR.doc,
          alpha: 0,
          link: '#introduction'
        },

        code: {
          label: 'code',
          color: CLR.branch,
          shape: "dot",
          alpha: 1
        },
        github: {
          color: CLR.code,
          alpha: 0,
          link: 'https://github.com/samizdatco/arbor'
        },
        ".zip": {
          color: CLR.code,
          alpha: 0,
          link: '/js/dist/arbor-v0.92.zip'
        },
        ".tar.gz": {
          color: CLR.code,
          alpha: 0,
          link: '/js/dist/arbor-v0.92.tar.gz'
        }
      },
      edges: {
        "arbor.js": {
          demos: {
            label: 'jj',
            length: .8,
            directed: true
          },
          docs: {
            label: 'doc',
            length: .8
          },
          code: {
            label: 'code',
            length: .8
          }
        },
        demos: {
          halfviz: {},
          atlas: {},
          echolalia: {}
        },
        docs: {
          reference: {},
          introduction: {}
        },
        code: {
          ".zip": {},
          ".tar.gz": {},
          "github": {}
        }
      }
    }
    sys.graft(theUI)

    // add some nodes to the graph and watch it go...
    // sys.addEdge('a', 'b')
    // sys.addEdge('a', 'c')
    // sys.addEdge('a', 'd')
    // sys.addEdge('a', 'e')
    // sys.addNode('f', {
    //   alone: true,
    //   mass: 10
    // })






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






    // or, equivalently:
    //
    // sys.graft({
    //   nodes:{
    //     f:{alone:true, mass:.25}
    //   }, 
    //   edges:{
    //     a:{ b:{},
    //         c:{},
    //         d:{},
    //         e:{}
    //     }
    //   }
    // })

  })

})(this.jQuery)