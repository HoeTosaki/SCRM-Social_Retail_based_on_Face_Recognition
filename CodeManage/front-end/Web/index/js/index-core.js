/**
 * 页面初始化
 */
$(function () {

    $('#sb-container').swatchbook({
        // number of degrees that is between each item
        angleInc: 15,
        neighbor: 15,
        // if it should be closed by default
        initclosed: false,
        // index of the element that when clicked, triggers the open/close function
        // by default there is no such element
        closeIdx: 10
    });

});