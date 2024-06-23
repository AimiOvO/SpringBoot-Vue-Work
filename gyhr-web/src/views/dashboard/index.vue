<template>
  <el-main height="''">
    <!-- Main content -->
    <div class="dashboard-container">
      <div class="dashboard-text">那是鸟，那是飞机，那是 “{{ name }}” ！</div>
    </div>
    <el-card shadow="always" style="margin-bottom: 30px;height: 170px;">
      <div style="margin-bottom: 20px;font-weight: bold;"> 功能捷径 </div>
      <el-row :gutter="20" type="flex" class="row-bg" justify="flex-start" style="margin-bottom: 80px;">
        <!-- 物业管理功能列表 -->
        <el-col :span="3" v-if="hasPerm('wy:rental:index')">
          <el-button class="col-buttom" type="primary" size="default"
            @click="() => { this.$router.push('/rental/CustomerList') }">
            <div class="show-header">
              <div class="show-num">{{ CustomerCount }}</div>
              <div class="bottom-text">租户总数</div>
            </div>
          </el-button>
        </el-col>
        <el-col :span="3" v-if="hasPerm('wy:rental:index')">
          <el-button class="col-buttom" type="primary" size="default"
            @click="() => { this.$router.push('/rental/OrderList') }">
            <div class="show-header">
              <div class="show-num">{{ OrderCount }}</div>
              <div class="bottom-text">在期租房单总数</div>
            </div>
          </el-button>
        </el-col>
        <el-col :span="3" v-if="hasPerm('wy:complaint')">
          <el-button class="col-buttom" type="primary" size="default"
            @click="() => { this.$router.push('/paas/ComplaintList') }">
            <div class="show-header">
              <div class="show-num">{{ ComplaintCount }}</div>
              <div class="bottom-text">待处理投诉总数</div>
            </div>
          </el-button>
        </el-col>
        <el-col :span="3" v-if="hasPerm('wy:repair')">
          <el-button class="col-buttom" type="primary" size="default"
            @click="() => { this.$router.push('/paas/RepairList') }">
            <div class="show-header">
              <div class="show-num">{{ RepairCount }}</div>
              <div class="bottom-text">待派修维修总数</div>
            </div>
          </el-button>
        </el-col>
        <!-- 业主功能列表 -->
        <el-col :span="3" v-if="hasPerm('wy:myfee')">
          <el-button class="col-buttom" type="primary" size="default"
            @click="() => { this.$router.push('/myfee/MyPowerFeeList') }">
            <div class="show-header">
              <div class="show-num">{{ FeePowerCount }}</div>
              <div class="bottom-text">待缴电费总数</div>
            </div>
          </el-button>
        </el-col>
        <el-col :span="3" v-if="hasPerm('wy:myfee')">
          <el-button class="col-buttom" type="primary" size="default"
            @click="() => { this.$router.push('/myfee/MyWaterFeeList') }">
            <div class="show-header">
              <div class="show-num">{{ FeeWaterCount }}</div>
              <div class="bottom-text">待缴水费总数</div>
            </div>
          </el-button>
        </el-col>
        <el-col :span="3" v-if="hasPerm('wy:myfee')">
          <el-button class="col-buttom" type="primary" size="default"
            @click="() => { this.$router.push('/myfee/MyRentalFeeList') }">
            <div class="show-header">
              <div class="show-num">{{ FeeRentalCount }}</div>
              <div class="bottom-text">待缴租凭费总数</div>
            </div>
          </el-button>
        </el-col>
        <el-col :span="3" v-if="hasPerm('wy:myfee')">
          <el-button class="col-buttom" type="primary" size="default"
            @click="() => { this.$router.push('/myfee/MyParkFeeList') }">
            <div class="show-header">
              <div class="show-num">{{ ParkFeeCount }}</div>
              <div class="bottom-text">待缴停车费总数</div>
            </div>
          </el-button>
        </el-col>
        <!-- 维修人员功能列表 -->
        <el-col :span="3" v-if="hasPerm('wy:dorepair')">
          <el-button class="col-buttom" type="primary" size="default"
            @click="() => { this.$router.push('/paas/DoRepairList') }">
            <div class="show-header">
              <div class="show-num">{{ DoRepairCount }}</div>
              <div class="bottom-text">待处理维修总数</div>
            </div>
          </el-button>
        </el-col>
      </el-row>
    </el-card>
    <el-card shadow="always" style="height: 800px;overflow: auto;">
      <div style="margin-bottom: 10px;font-weight: bold;"> 公告列表 </div>
      <el-collapse accordion>
        <el-collapse-item v-for="item in this.tableList" :key="item.noticeId"
          :title='item.uname + " —— " + item.createTime.split(" ")[0] + " ———— " + item.title'>
          <pre style="font: 1em sans-serif;">{{ item.content }}</pre>
        </el-collapse-item>
      </el-collapse>
    </el-card>
  </el-main>
</template>

<script>
import { mapGetters } from 'vuex'
import { getNoticeListApi } from "@/api/paas/notice"
import {
  getOrderCountApi, getComplaintCountApi, getRepairCountApi, getCustomerCountApi,
  getFeePowerCountApi, getFeeWaterCountApi, getFeeRentalCountApi, getParkFeeCountApi,
  getDoRepairCountApi
} from '@/api/dashboard/dashboard'
import hasPermission from '@/permission/index'
import { getUserId } from "@/utils/auth";

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  data() {
    return {
      //表格数据
      tableList: [],
      //列表查询参数
      parms: {
        currentPage: 1,
        pageSize: 10,
      },
      OrderCount: 0,
      ComplaintCount: 0,
      RepairCount: 0,
      CustomerCount: 0,

      FeePowerCount: 0,
      FeeWaterCount: 0,
      FeeRentalCount: 0,
      ParkFeeCount: 0,

      DoRepairCount: 0,
    }
  },
  created() {
    this.getListData();
    this.getCustomerCount();
    this.getOrderCount();
    this.getComplaintCount();
    this.getRepairCount();

    this.getMyFeeCount();

    this.getDoRepairCount();
  },
  methods: {
    async getCustomerCount() {
      if (!hasPermission('wy:rental:index')) {
        return
      }
      let res = await getCustomerCountApi();
      if (res && res.code == 200) {
        this.CustomerCount = res.data;
      }
    },
    async getOrderCount() {
      if (!hasPermission('wy:rental:index')) {
        return
      }
      let res = await getOrderCountApi();
      if (res && res.code == 200) {
        this.OrderCount = res.data;
      }
    },
    async getComplaintCount() {
      if (!hasPermission('wy:complaint')) {
        return
      }
      let res = await getComplaintCountApi();
      if (res && res.code == 200) {
        this.ComplaintCount = res.data;
      }
    },
    async getRepairCount() {
      if (!hasPermission('wy:repair')) {
        return
      }
      let res = await getRepairCountApi();
      if (res && res.code == 200) {
        this.RepairCount = res.data;
      }
    },

    async getMyFeeCount() {
      if (!hasPermission('wy:myfee')) {
        return
      }
      let res = await getFeePowerCountApi({ customerId: getUserId() });
      if (res && res.code == 200) {
        this.FeePowerCount = res.data;
      }
      res = await getFeeWaterCountApi({ customerId: getUserId() });
      if (res && res.code == 200) {
        this.FeeWaterCount = res.data;
      }
      res = await getFeeRentalCountApi({ customerId: getUserId() });
      if (res && res.code == 200) {
        this.FeeRentalCount = res.data;
      }
      res = await getParkFeeCountApi({ customerId: getUserId() });
      if (res && res.code == 200) {
        this.ParkFeeCount = res.data;
      }
    },

    async getDoRepairCount() {
      if (!hasPermission('wy:dorepair')) {
        return
      }
      let res = await getDoRepairCountApi({ userId: getUserId() });
      if (res && res.code == 200) {
        this.DoRepairCount = res.data;
      }
    },

    async getListData() {
      let res = await getNoticeListApi(this.parms);
      if (res && res.code == 200) {
        this.tableList = res.data.records;
      }
    },
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }

  &-text {
    font-size: 30px;
    line-height: 46px;
    font-weight: bold;
  }
}

.col-buttom {
  border: #000 solid 1px;
  border-radius: 5px;
  padding: 0;
  width: 100%;
}

.bottom-text {
  bottom: 0;
  width: 100%;
  height: 25px;
  line-height: 25px;
  text-align: center;
  position: absolute;
  font-weight: 600;
  background: rgba(0, 0, 0, 0.1);

}

.show-header {
  background: #00c0ef;
  color: #fff;
  height: 80px;
  border-radius: 5px;
  position: relative;
}

.show-num {
  text-align: left;
  font-size: 30px;
  font-weight: 600;
  padding: 5px;
}
</style>
