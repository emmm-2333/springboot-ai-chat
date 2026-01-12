<template>
  <!-- 登录页面布局 -->
  <div class="auth-page">
    <el-card class="auth-card">
      <h2 class="title">登录</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <!-- 用户名输入框 -->
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" autocomplete="username" />
        </el-form-item>
        <!-- 密码输入框 -->
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" autocomplete="current-password" />
        </el-form-item>
        <!-- 登录按钮 -->
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="onSubmit" style="width:100%">登录</el-button>
        </el-form-item>
        <!-- 注册链接 -->
        <el-link type="primary" @click="$router.push('/register')">没有账号？去注册</el-link>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { login } from '../api/auth';
import { useUserStore } from '../stores/user';
import { useRouter } from 'vue-router';

const router = useRouter();
const store = useUserStore();
const formRef = ref();
const loading = ref(false);
const form = reactive({ username: '', password: '' });
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
};

const onSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return;
    loading.value = true;
    try {
      const res = await login(form);
      store.setToken(res.data.token);
      store.setProfile(res.data.user);
      ElMessage.success('登录成功');
      router.push('/chat');
    } catch (err) {
      // error handled globally
    } finally {
      loading.value = false;
    }
  });
};
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #eef1f6, #dfe8ff);
}
.auth-card {
  width: 360px;
}
.title {
  text-align: center;
  margin-bottom: 16px;
}
</style>
