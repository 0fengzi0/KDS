<template>
    <view>
        <view class="kds-list full-width kds-kds-list">
            <button class="d-flex kds-list-item kds-button " type="default" @click="plusKds">+</button>
            <input class="d-flex kds-number-input kds-list-item" type="number" v-model="kdsNumber" maxlength="4" />
            <button class="d-flex kds-list-item kds-button" type="default" @click="lessKds">-</button>
        </view>
        <view class="mt-100 mlr-20 do-kds flex">
            <button type="default" class="bg-green d-flex mlr-20" @click="play">叫号</button>
            <button type="default" class="bg-blue d-flex mlr-20" @click="pass">取餐</button>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            kdsNumber: 1,
            audioNumber: 0,
            audioList: ['q', '0', '1', '8', '8', 'qc'],
            innerAudioContext: null,
            // 重复播放次数
            repeatNumber: 0
        };
    },
    computed: {},
    mounted() {
        this.$nextTick(() => {
            this.innerAudioContext = uni.createInnerAudioContext();
            this.innerAudioContext.onPlay(() => {
                this.onPlay();
            });
            this.innerAudioContext.onEnded(() => {
                this.onEnded();
            });
        });
    },
    methods: {
        plusKds() {
            this.kdsNumber++;
        },
        lessKds() {
            this.kdsNumber > 1 ? this.kdsNumber-- : 1;
        },
        play() {
            console.debug('开始叫号');

            let kdsStringNumber = (Array(4).join('0') + this.kdsNumber).slice(-4);

            this.audioList.splice(1, 4, ...kdsStringNumber.split(''));

            this.audioNumber = 0;
            this.repeatNumber = 0;
            this.innerAudioContext.src = '../../static/audio/' + this.audioList[this.audioNumber] + '.mp3';
            this.innerAudioContext.play();
        },

        pass() {
            console.debug('下一个');
            this.plusKds();
            uni.showToast({
                icon: 'success',
                title: '已取餐'
            });
        },

        onEnded() {
            console.debug('播放结束');
            // 切换音频
            if (this.audioNumber < this.audioList.length - 1) {
                this.audioNumber++;
                this.innerAudioContext.src = '../../static/audio/' + this.audioList[this.audioNumber] + '.mp3';
                this.innerAudioContext.play();
            } else if (this.repeatNumber === 0) {
                this.repeatNumber = 1;
                this.audioNumber = 0;
                this.innerAudioContext.src = '../../static/audio/' + this.audioList[this.audioNumber] + '.mp3';
                this.innerAudioContext.play();
            }
        },

        onPlay() {
            console.debug('开始播放');
        }
    }
};
</script>

<style>
.kds-list {
    display: flex;
    justify-content: center;
}

.kds-kds-list {
    height: 100rpx;
}

.kds-number-input {
    text-align: center;
    flex: 0 0 200rpx !important;
    font-size: 72rpx;
}

.kds-button {
    flex: 0 0 100rpx !important;
}

.kds-list-item {
    height: 100%;
    line-height: 100rpx;
    margin: 0;
}
</style>
