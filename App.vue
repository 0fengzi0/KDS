<script>
export default {
    onLaunch: function() {
        console.log('App Launch');
    },
    onShow: function() {
        console.log('App Show');
    },
    onHide: function() {
        console.log('App Hide');
    }
};
</script>

<style>
/*每个页面公共css */
.flex {
    display: flex;
}

.d-flex {
    flex: 1;
}

.full-width {
    width: 100%;
}

.bg-green {
    background-color: #4caf50 !important;
    color: #ffffff !important;
}

.bg-blue {
    background-color: #1976d2 !important;
    color: #ffffff !important;
}

.mt-20 {
    margin-top: 20rpx;
}

.mlr-20 {
    margin-left: 20rpx;
    margin-right: 20rpx;
}

.ma-a{
    margin: auto;
}

.mlf-a{
    margin-left: auto;
    margin-right: auto;
}

.mtb-a{
    margin-top: auto;
    margin-bottom: auto;
}

.mt-100{
    margin-top: 100rpx;
}
</style>
