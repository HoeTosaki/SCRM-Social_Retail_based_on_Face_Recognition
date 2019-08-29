/**
 * 页面初始化
 */
document.userid = window.location.search.split('?')[1];
alert(document.userid);

/**
 * 瀑布流初始化
 */
jQuery(document).ready(function($){
	//final width --> this is the quick view image slider width
	//maxQuickWidth --> this is the max-width of the quick-view panel
	var sliderFinalWidth = 400,
		maxQuickWidth = 900;

	//open the quick view panel
	$('.cd-trigger').on('click', function(event){
		var selectedImage = $(this).parent('.cd-item').children('img'),
			slectedImageUrl = selectedImage.attr('src');

		$('body').addClass('overlay-layer');
		animateQuickView(selectedImage, sliderFinalWidth, maxQuickWidth, 'open');

		//update the visible slider image in the quick view panel
		//you don't need to implement/use the updateQuickView if retrieving the quick view data with ajax
		updateQuickView(slectedImageUrl);
	});

	//close the quick view panel
	$('body').on('click', function(event){
		if( $(event.target).is('.cd-close') || $(event.target).is('body.overlay-layer')) {
			closeQuickView( sliderFinalWidth, maxQuickWidth);
		}
	});
	$(document).keyup(function(event){
		//check if user has pressed 'Esc'
    	if(event.which=='27'){
			closeQuickView( sliderFinalWidth, maxQuickWidth);
		}
	});

	//quick view slider implementation
	$('.cd-quick-view').on('click', '.cd-slider-navigation a', function(){
		updateSlider($(this));
	});

	//center quick-view on window resize
	$(window).on('resize', function(){
		if($('.cd-quick-view').hasClass('is-visible')){
			window.requestAnimationFrame(resizeQuickView);
		}
	});

	function updateSlider(navigation) {
		var sliderConatiner = navigation.parents('.cd-slider-wrapper').find('.cd-slider'),
			activeSlider = sliderConatiner.children('.selected').removeClass('selected');
		if ( navigation.hasClass('cd-next') ) {
			( !activeSlider.is(':last-child') ) ? activeSlider.next().addClass('selected') : sliderConatiner.children('li').eq(0).addClass('selected');
		} else {
			( !activeSlider.is(':first-child') ) ? activeSlider.prev().addClass('selected') : sliderConatiner.children('li').last().addClass('selected');
		}
	}

	function updateQuickView(url) {
		$('.cd-quick-view .cd-slider li').removeClass('selected').find('img[src="'+ url +'"]').parent('li').addClass('selected');
	}

	function resizeQuickView() {
		var quickViewLeft = ($(window).width() - $('.cd-quick-view').width())/2,
			quickViewTop = ($(window).height() - $('.cd-quick-view').height())/2;
		$('.cd-quick-view').css({
		    "top": quickViewTop,
		    "left": quickViewLeft,
		});
	}

	function closeQuickView(finalWidth, maxQuickWidth) {
		var close = $('.cd-close'),
			activeSliderUrl = close.siblings('.cd-slider-wrapper').find('.selected img').attr('src'),
			selectedImage = $('.empty-box').find('img');
		//update the image in the gallery
		if( !$('.cd-quick-view').hasClass('velocity-animating') && $('.cd-quick-view').hasClass('add-content')) {
			selectedImage.attr('src', activeSliderUrl);
			animateQuickView(selectedImage, finalWidth, maxQuickWidth, 'close');
		} else {
			closeNoAnimation(selectedImage, finalWidth, maxQuickWidth);
		}
	}

	function animateQuickView(image, finalWidth, maxQuickWidth, animationType) {
		//store some image data (width, top position, ...)
		//store window data to calculate quick view panel position
		var parentListItem = image.parent('.cd-item'),
			topSelected = image.offset().top - $(window).scrollTop(),
			leftSelected = image.offset().left,
			widthSelected = image.width(),
			heightSelected = image.height(),
			windowWidth = $(window).width(),
			windowHeight = $(window).height(),
			finalLeft = (windowWidth - finalWidth)/2,
			finalHeight = finalWidth * heightSelected/widthSelected,
			finalTop = (windowHeight - finalHeight)/2,
			quickViewWidth = ( windowWidth * .8 < maxQuickWidth ) ? windowWidth * .8 : maxQuickWidth ,
			quickViewLeft = (windowWidth - quickViewWidth)/2;

		if( animationType == 'open') {
			//hide the image in the gallery
			parentListItem.addClass('empty-box');
			//place the quick view over the image gallery and give it the dimension of the gallery image
			$('.cd-quick-view').css({
			    "top": topSelected,
			    "left": leftSelected,
			    "width": widthSelected,
			}).velocity({
				//animate the quick view: animate its width and center it in the viewport
				//during this animation, only the slider image is visible
			    'top': finalTop+ 'px',
			    'left': finalLeft+'px',
			    'width': finalWidth+'px',
			}, 1000, [ 400, 20 ], function(){
				//animate the quick view: animate its width to the final value
				$('.cd-quick-view').addClass('animate-width').velocity({
					'left': quickViewLeft+'px',
			    	'width': quickViewWidth+'px',
				}, 300, 'ease' ,function(){
					//show quick view content
					$('.cd-quick-view').addClass('add-content');
				});
			}).addClass('is-visible');
		} else {
			//close the quick view reverting the animation
			$('.cd-quick-view').removeClass('add-content').velocity({
			    'top': finalTop+ 'px',
			    'left': finalLeft+'px',
			    'width': finalWidth+'px',
			}, 300, 'ease', function(){
				$('body').removeClass('overlay-layer');
				$('.cd-quick-view').removeClass('animate-width').velocity({
					"top": topSelected,
				    "left": leftSelected,
				    "width": widthSelected,
				}, 500, 'ease', function(){
					$('.cd-quick-view').removeClass('is-visible');
					parentListItem.removeClass('empty-box');
				});
			});
		}
	}
	function closeNoAnimation(image, finalWidth, maxQuickWidth) {
		var parentListItem = image.parent('.cd-item'),
			topSelected = image.offset().top - $(window).scrollTop(),
			leftSelected = image.offset().left,
			widthSelected = image.width();

		//close the quick view reverting the animation
		$('body').removeClass('overlay-layer');
		parentListItem.removeClass('empty-box');
		$('.cd-quick-view').velocity("stop").removeClass('add-content animate-width is-visible').css({
			"top": topSelected,
		    "left": leftSelected,
		    "width": widthSelected,
		});
	}
});



void function (window, document, undefined) {

	// ES5 strict mode
	"user strict";

	var MIN_COLUMN_COUNT = 2; // minimal column count
	var COLUMN_WIDTH = 152.5; // cell width: 190, padding: 14 * 2, border: 1 * 2
	var CELL_PADDING = 26; // cell padding: 14 + 10, border: 1 * 2
	var GAP_HEIGHT = 5; // vertical gap between cells
	var GAP_WIDTH = 5; // horizontal gap between cells
	var THRESHOLD = 2000; // determines whether a cell is too far away from viewport (px)

	var columnHeights; // array of every column's height
	var columnCount; // number of columns
	var noticeDelay; // popup notice timer
	var resizeDelay; // resize throttle timer
	var scrollDelay; // scroll throttle timer
	var managing = false; // flag for managing cells state
	var loading = false; // flag for loading cells state

	var noticeContainer = document.getElementById('notice');
	var cellsContainer = document.getElementById('cells');
	var cellTemplate = document.getElementById('template').innerHTML;

	// Cross-browser compatible event handler.
	var addEvent = function (element, type, handler) {
		if (element.addEventListener) {
			addEvent = function (element, type, handler) {
				element.addEventListener(type, handler, false);
			};
		} else if (element.attachEvent) {
			addEvent = function (element, type, handler) {
				element.attachEvent('on' + type, handler);
			};
		} else {
			addEvent = function (element, type, handler) {
				element['on' + type] = handler;
			};
		}
		addEvent(element, type, handler);
	};

	// Get the minimal value within an array of numbers.
	var getMinVal = function (arr) {
		return Math.min.apply(Math, arr);
	};

	// Get the maximal value within an array of numbers.
	var getMaxVal = function (arr) {
		return Math.max.apply(Math, arr);
	};

	// Get index of the minimal value within an array of numbers.
	var getMinKey = function (arr) {
		var key = 0;
		var min = arr[0];
		for (var i = 1, len = arr.length; i < len; i++) {
			if (arr[i] < min) {
				key = i;
				min = arr[i];
			}
		}
		return key;
	};

	// Get index of the maximal value within an array of numbers.
	var getMaxKey = function (arr) {
		var key = 0;
		var max = arr[0];
		for (var i = 1, len = arr.length; i < len; i++) {
			if (arr[i] > max) {
				key = i;
				max = arr[i];
			}
		}
		return key;
	};

	// Pop notice tag after user liked or marked an item.
	var updateNotice = function (event) {
		clearTimeout(noticeDelay);
		var e = event || window.event;
		var target = e.target || e.srcElement;
		if (target.tagName == 'SPAN') {
			var targetTitle = target.parentNode.tagLine;
			noticeContainer.innerHTML = (target.className == 'like' ? 'Liked ' : 'Marked ') + '<strong>' + targetTitle + '</strong>';
			noticeContainer.className = 'on';
			noticeDelay = setTimeout(function () {
				noticeContainer.className = 'off';
			}, 2000);
		}
	};

	// Calculate column count from current page width.
	var getColumnCount = function () {
		return Math.max(MIN_COLUMN_COUNT, Math.floor((document.body.offsetWidth + GAP_WIDTH) / (COLUMN_WIDTH + GAP_WIDTH)));
		//		return MIN_COLUMN_COUNT
	};

	// Reset array of column heights and container width.
	var resetHeights = function (count) {
		columnHeights = [];
		for (var i = 0; i < count; i++) {
			columnHeights.push(0);
		}
		console.log(count)
		cellsContainer.style.width = (count * (COLUMN_WIDTH + GAP_WIDTH) - GAP_WIDTH) + 'px';
	};

	// Fetch JSON string via Ajax, parse to HTML and append to the container.
	// var appendCells = function (num) {
	//   if (loading) {
	//     // Avoid sending too many requests to get new cells.
	//     return;
	//   }
	//   var xhrRequest = new XMLHttpRequest();
	//   var fragment = document.createDocumentFragment();
	//   var cells = [];
	//   var images;
	//   xhrRequest.open('GET', 'json.php?n=' + num, true);
	//   xhrRequest.onreadystatechange = function () {
	//     if (xhrRequest.readyState == 4 && xhrRequest.status == 200) {
	//       images = JSON.parse(xhrRequest.responseText);
	//       for (var j = 0, k = images.length; j < k; j++) {
	//         var cell = document.createElement('div');
	//         cell.className = 'cell pending';
	//         cell.tagLine = images[j].title;
	//         cells.push(cell);
	//         console.log(images[j])
	//         front(cellTemplate, images[j], cell);
	//         fragment.appendChild(cell);
	//       }
	//       cellsContainer.appendChild(fragment);
	//       loading = false;
	//       adjustCells(cells);
	//     }
	//   };
	//   loading = true;
	//   xhrRequest.send(null);
	// };

	// my append cells via JQuery.
	var appendCells = function (num) {
		if (loading) {
			// Avoid sending too many requests to get new cells.
			return;
		}
		var fragment = document.createDocumentFragment();
		var cells = [];
		var images;

		$.get("rcmdPull", {
			userid: document.userid,
			pointer: cells.length
		}, function (result) {
			//alert(result);
			images = JSON.parse(result);
			for (var j = 0, k = images.length; j < k; j++) {
				var cell = document.createElement('div');
				cell.className = 'cell pending';
				cell.tagLine = images[j].title;
				cells.push(cell);
				console.log(images[j])
				front(cellTemplate, images[j], cell);
				fragment.appendChild(cell);
			}
			cellsContainer.appendChild(fragment);
			loading = false;
			adjustCells(cells);
		});
		loading = true;
		xhrRequest.send(null);
	};


	// Fake mode, only for GitHub demo. Delete this function in your project.
	var appendCellsDemo = function (num) {
		if (loading) {
			// Avoid sending too many requests to get new cells.
			return;
		}
		var fragment = document.createDocumentFragment();
		var cells = [];
		var images = [0, 286, 143, 270, 143, 190, 285, 152, 275, 285, 285, 128, 281, 242, 339, 236, 157, 286, 259, 267, 137, 253, 127, 190, 190, 225, 269, 264, 272, 126, 265, 287, 269, 125, 285, 190, 314, 141, 119, 274, 274, 285, 126, 279, 143, 266, 279, 600, 276, 285, 182, 143, 287, 126, 190, 285, 143, 241, 166, 240, 190];
		for (var j = 0; j < 10; j++) {
			console.log(key)
			var key = Math.floor(Math.random() * 20) + 1;
			var cell = document.createElement('div');
			cell.className = 'cell pending';
			cell.tagLine = 'demo picture ' + key;
			cells.push(cell);
			front(cellTemplate, {
				'title': 'demo picture ' + key,
				'src': key,
				'height': "auto",
				'width': 122.5
			}, cell);
			fragment.appendChild(cell);
		}
		// Faking network latency.
		setTimeout(function () {
			loading = false;
			cellsContainer.appendChild(fragment);
			adjustCells(cells);
		}, 1000);
	};

	// Position the newly appended cells and update array of column heights.
	var adjustCells = function (cells, reflow) {
		var columnIndex;
		var columnHeight;
		for (var j = 0, k = cells.length; j < k; j++) {
			// Place the cell to column with the minimal height.
			columnIndex = getMinKey(columnHeights);
			columnHeight = columnHeights[columnIndex];
			cells[j].style.height = (cells[j].offsetHeight - CELL_PADDING) + 'px';
			cells[j].style.left = columnIndex * (COLUMN_WIDTH + GAP_WIDTH) + 'px';
			cells[j].style.top = columnHeight + 'px';
			columnHeights[columnIndex] = columnHeight + GAP_HEIGHT + cells[j].offsetHeight;
			if (!reflow) {
				cells[j].className = 'cell ready';
			}
		}
		cellsContainer.style.height = getMaxVal(columnHeights) + 'px';
		manageCells();
	};

	// Calculate new column data if it's necessary after resize.
	var reflowCells = function () {
		// Calculate new column count after resize.
		columnCount = getColumnCount();
		if (columnHeights.length != columnCount) {
			// Reset array of column heights and container width.
			resetHeights(columnCount);
			adjustCells(cellsContainer.children, true);
		} else {
			manageCells();
		}
	};

	// Toggle old cells' contents from the DOM depending on their offset from the viewport, save memory.
	// Load and append new cells if there's space in viewport for a cell.
	var manageCells = function () {
		// Lock managing state to avoid another async call. See {Function} delayedScroll.
		managing = true;

		var cells = cellsContainer.children;
		var viewportTop = (document.body.scrollTop || document.documentElement.scrollTop) - cellsContainer.offsetTop;
		var viewportBottom = (window.innerHeight || document.documentElement.clientHeight) + viewportTop;

		// Remove cells' contents if they are too far away from the viewport. Get them back if they are near.
		// TODO: remove the cells from DOM should be better :<
		for (var i = 0, l = cells.length; i < l; i++) {
			if ((cells[i].offsetTop - viewportBottom > THRESHOLD) || (viewportTop - cells[i].offsetTop - cells[i].offsetHeight > THRESHOLD)) {
				if (cells[i].className === 'cell ready') {
					cells[i].fragment = cells[i].innerHTML;
					cells[i].innerHTML = '';
					cells[i].className = 'cell shadow';
				}
			} else {
				if (cells[i].className === 'cell shadow') {
					cells[i].innerHTML = cells[i].fragment;
					cells[i].className = 'cell ready';
				}
			}
		}

		// If there's space in viewport for a cell, request new cells.
		if (viewportBottom > getMinVal(columnHeights)) {
			// Remove the if/else statement in your project, just call the appendCells function.
			// if (isGithubDemo) {
			//   appendCellsDemo(columnCount);
			// } else {
			//   appendCells(columnCount);
			// }
			appendCells(columnCount);
		}

		// Unlock managing state.
		managing = false;
	};

	// Add 500ms throttle to window scroll.
	var delayedScroll = function () {
		clearTimeout(scrollDelay);
		if (!managing) {
			// Avoid managing cells for unnecessity.
			scrollDelay = setTimeout(manageCells, 500);
		}
	};

	// Add 500ms throttle to window resize.
	var delayedResize = function () {
		clearTimeout(resizeDelay);
		resizeDelay = setTimeout(reflowCells, 500);
	};

	// Initialize the layout.
	var init = function () {
		// Add other event listeners.
		addEvent(cellsContainer, 'click', updateNotice);
		addEvent(window, 'resize', delayedResize);
		addEvent(window, 'scroll', delayedScroll);

		// Initialize array of column heights and container width.
		columnCount = getColumnCount();
		resetHeights(columnCount);

		// Load cells for the first time.
		manageCells();
	};

	// Ready to go!
	addEvent(window, 'load', init);

}(window, document);



