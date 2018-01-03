$(function () {
    alert("load");
    $('#table').bootstrapTable({
        columns: [{
            field: 'id',
            title: 'Item ID'
        }, {
            field: 'name',
            title: 'Item Name'
        }, {
            field: 'price',
            title: 'Item Price'
        }],
        data: [{
            id: 1,
            name: 'Item 1',
            price: '$1'
        }, {
            id: 2,
            name: 'Item 2',
            price: '$2'
        }]
    });




})



/*
$('#table').bootstrapTable({
    url: 'http://localhost/happyFood/queryButtonByPage',
    columns: [{
        field: 'id',
        title: 'Item ID'
    }, {
        field: 'name',
        title: 'Item Name'
    }, {
        field: 'price',
        title: 'Item Price'
    },],
    ajax: {
        url: 'http://localhost/happyFood/queryButtonByPage',
        type: "post",
        data: function (data) {
            $.xDataTablesParams(data, $.trim($('#searchInput').val()));
            var $storeId = $("input[name='storeIds']").val();
            if ($storeId != '') {
                data['search.storeId'] = $storeId;
            }
            if ($.trim($('#searchInput').val()) != null)
                data['search.tableNo'] = $.trim($('#searchInput').val());
            return data;
        }
    }
});

*/


