<template>
  <!-- 注册页面布局 -->
  <div class="auth-page">
    <el-card class="auth-card">
      <h2 class="title">注册</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <!-- 用户名输入框 -->
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" />
        </el-form-item>
        <!-- 密码输入框 -->
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" />
        </el-form-item>
        <!-- 邮箱输入框 -->
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" />
        </el-form-item>
        <!-- 昵称输入框 -->
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" />
        </el-form-item>
        <!-- 注册按钮 -->
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="onSubmit" style="width:100%">注册</el-button>
        </el-form-item>
        <!-- 登录链接 -->
        <el-link type="primary" @click="$router.push('/login')">已有账号？去登录</el-link>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { register } from '../api/auth';
import { useRouter } from 'vue-router';

const router = useRouter();
const formRef = ref();
const loading = ref(false);
const form = reactive({ username: '', password: '', email: '', nickname: '' });
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }]
};

const onSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return;
    loading.value = true;
    try {
      await register(form);
      ElMessage.success('注册成功，请登录');
      router.push('/login');
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
  width: 400px;
}
.title {
  text-align: center;
  margin-bottom: 16px;
}
</style>
