var width = $(window).width();  
var height = $(window).height();  
  
var html = "<div id='PageLoadingTip' style='position: absolute; z-index: 1000; top: 0px; left: 0px; width: 100%; height: 100%; background: gray; text-align: center;'>";  
html += "<h1 style='top: 48%; position: relative;color:#15428B;'>页面加载中,请稍候···</h1>";  
html += "</div>";  

window.onload = function () {  
    var mask = document.getElementById('PageLoadingTip');  
    mask.parentNode.removeChild(mask);  
};  
document.write(html);  