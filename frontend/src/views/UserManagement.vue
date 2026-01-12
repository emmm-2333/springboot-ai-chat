<template>
  <!-- 用户管理页面布局 -->
  <el-container class="page">
    <!-- 页面头部 -->
    <el-header class="header">
      <div>用户管理</div>
      <div>
        <el-button type="primary" @click="openDialog()">新增用户</el-button>
        <el-button @click="goChat">返回对话</el-button>
      </div>
    </el-header>
    <!-- 页面主体 -->
    <el-main>
      <!-- 搜索框 -->
      <el-input v-model="search" placeholder="搜索用户名/昵称" clearable style="margin-bottom:12px" />
      <!-- 用户表格 -->
      <el-table :data="filtered" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="status" label="状态" />
        <el-table-column label="操作" width="160">
          <template #default="scope">
            <el-button link size="small" @click="openDialog(scope.row)">编辑</el-button>
            <el-popconfirm title="确认删除?" @confirm="onDelete(scope.row.id)">
              <template #reference>
                <el-button link type="danger" size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页组件 -->
      <div style="margin-top:12px; text-align:right;">
        <el-pagination
          layout="prev, pager, next"
          :total="total"
          :page-size="query.size"
          :current-page="query.page"
          @current-change="onPageChange"
        />
      </div>
    </el-main>
  </el-container>

  <!-- 用户编辑/新增对话框 -->
  <el-dialog v-model="dialog.visible" :title="dialog.form.id ? '编辑用户' : '新增用户'" width="500px">
    <el-form :model="dialog.form" label-width="80px" :rules="rules" ref="formRef">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="dialog.form.username" :disabled="!!dialog.form.id" />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="dialog.form.password" type="password" placeholder="不修改则留空" />
      </el-form-item>
      <el-form-item label="昵称" prop="nickname">
        <el-input v-model="dialog.form.nickname" />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="dialog.form.email" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="dialog.form.status">
          <el-option :value="1" label="启用" />
          <el-option :value="0" label="停用" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialog.visible=false">取消</el-button>
      <el-button type="primary" :loading="dialog.loading" @click="onSave">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { fetchUsers, createUser, updateUser, deleteUser } from '../api/user';
import { useRouter } from 'vue-router';

const router = useRouter();
const query = reactive({ page: 1, size: 10 });
const list = ref([]);
const total = ref(0);
const search = ref('');
const formRef = ref();
const dialog = reactive({ visible: false, loading: false, form: {} });

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }]
};

const load = async () => {
  const res = await fetchUsers(query);
  list.value = res.data.items;
  total.value = res.data.total;
};

onMounted(load);

const filtered = computed(() => {
  if (!search.value) return list.value;
  return list.value.filter(u =>
    (u.username || '').includes(search.value) ||
    (u.nickname || '').includes(search.value)
  );
});

const openDialog = (row = {}) => {
  dialog.form = { ...row };
  if (!dialog.form.status) dialog.form.status = 1;
  dialog.visible = true;
};

const onSave = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return;
    dialog.loading = true;
    try {
      if (dialog.form.id) {
        await updateUser(dialog.form.id, dialog.form);
        ElMessage.success('更新成功');
      } else {
        await createUser(dialog.form);
        ElMessage.success('创建成功');
      }
      dialog.visible = false;
      await load();
    } finally {
      dialog.loading = false;
    }
  });
};

const onDelete = async (id) => {
  await deleteUser(id);
  ElMessage.success('删除成功');
  await load();
};

const onPageChange = (page) => {
  query.page = page;
  load();
};

const goChat = () => router.push('/chat');
</script>

<style scoped>
.page { min-height: 100vh; }
.header { display:flex; justify-content:space-between; align-items:center; background:#fff; box-shadow:0 1px 4px rgba(0,0,0,0.08); }
</style>
