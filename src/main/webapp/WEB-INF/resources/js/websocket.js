if ('WebSocket' in window) {

    let store = JSON.parse(sessionStorage.getItem('employee')).st_no;

    var host = window.location.host;
    var path2 = window.location.pathname;
    var webCtx = path2.substring(0, path2.indexOf('/', 1));
    websocket = new WebSocket(`ws://${host}${webCtx}/websocket/${store}`);


    //连接发生错误的回调方法
    websocket.onerror = function () {
        setMessageInnerHTML("WebSocket連線錯誤");
    };
    //连接成功建立的回调方法
    websocket.onopen = function () {
        // setMessageInnerHTML("WebSocket連線成功");
    }
    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        setMessageInnerHTML(event.data);
    }
    //连接关闭的回调方法
    websocket.onclose = function () {
        // setMessageInnerHTML("WebSocket連線關閉");
    }
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        closeWebSocket();
    }
    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML) {
        document.getElementById('notice').innerHTML += `
            <div class=" fade show toast " role="alert" aria-live="assertive" aria-atomic="true" autohide= 'false'>
                <div class="toast-header">
                <strong class="me-auto">Family Rent</strong>
                <button type="button"  class="btn-close wbclose" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
                <div class="toast-body">`+ innerHTML + `</div>
            </div>
            `;
        // document.getElementById('notice').innerHTML += `
        //     <div class=" fade show toast position-absolute " role="alert" aria-live="assertive" aria-atomic="true" autohide= 'false' style="right: 5px; bottom: 5px;">
        //         <div class="toast-header">
        //         <strong class="me-auto">Family Rent</strong>
        //         <button type="button"  class="btn-close wbclose" data-bs-dismiss="toast" aria-label="Close"></button>
        //         </div>
        //         <div class="toast-body">`+
        //     innerHTML +
        //     `</div>
        //     </div>
        //     `;

        const cr = window.location.pathname.indexOf('cardispatchrecord.jsp')
        console.log('in')
        if (cr >= 1) {
            if(innerHTML.indexOf('出車') >= 1){
                let no = innerHTML.substring(innerHTML.indexOf('單號:')+3,innerHTML.indexOf('出車里程')-1);
                let miles = innerHTML.substring(innerHTML.indexOf('里程:')+3);
                $('#applyrecordTab').find(`#${no}`).find('.status').text('出車');
                $('#applyrecordTab').find(`#${no}`).find('.miles_before').text(miles);
                $('#applyrecordTab').find(`#${no}`).children().eq(11).text('');
                $('#applyrecordTab').find(`#${no}`).children().eq(12).html(`<button type="button" class="btn btn-outline-secondary btn-sm send returncar"  data-bs-toggle="modal" data-bs-target="#exampleModal">還車</button>`)
            }
        }

    }
    //关闭WebSocket连接
    function closeWebSocket() {
        websocket.close();
    }
    //发送消息
    function send() {
        var message = document.getElementById('text').value;
        websocket.send(message);
    }

    // document.getElementById('notice').addEventListener('click',function(e){
    //     console.log('in click')
    //     const target = e.target;
    //     if (target.classList.contains('wbclose')) {
    //         const cr = window.location.pathname.indexOf('cardispatchrecord.jsp')
    //         console.log('in')
    //         if(cr >= 1){
    //             window.location.reload();
    //         }
    //     }

    // })

}
else {
    alert('当前浏览器 Not support websocket')
}