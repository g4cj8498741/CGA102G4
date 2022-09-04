$('#showFu').on('click','.lss',function (e) {
    const target = e.target;
    if (target.classList.contains('lss')) {
        $(this).children('div.div2').slideToggle(500);
    }
})