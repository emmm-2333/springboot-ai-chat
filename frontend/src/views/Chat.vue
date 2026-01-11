<template>
  <el-container class="chat-page">
    <el-aside width="260px" class="sidebar">
      <div class="sidebar-header">
        <div class="logo">AI Chat</div>
        <el-button type="primary" size="small" @click="newConversation">新建对话</el-button>
      </div>
      <el-scrollbar height="calc(100vh - 80px)">
        <el-menu :default-active="String(activeId)" class="conversation-menu">
          <el-menu-item v-for="c in conversations" :key="c.id" :index="String(c.id)" @click="switchConversation(c.id)">
            <span>{{ c.title }}</span>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>
    <el-container>
      <el-header class="topbar">
        <div class="title">{{ activeConversationTitle }}</div>
        <div class="actions">
          <el-button type="primary" link @click="goUsers">用户管理</el-button>
          <el-button type="danger" link @click="logout">退出</el-button>
        </div>
      </el-header>
      <el-main class="chat-body">
        <el-scrollbar ref="scrollRef" class="messages">
          <div v-for="m in messages" :key="m.id + m.role + m.content" :class="['msg', m.role]">
            <div class="bubble">
              <div class="role">{{ m.role === 'user' ? '我' : 'AI' }}</div>
              <div class="content">{{ m.content }}</div>
            </div>
          </div>
        </el-scrollbar>
      </el-main>
      <el-footer class="chat-input">
        <el-input
          v-model="input"
          type="textarea"
          :rows="3"
          placeholder="输入消息，Enter发送，Shift+Enter换行"
          @keyup.enter.exact.prevent="onSend"
          @keyup.enter.shift.exact="()=>{}"
        />
        <div class="input-actions">
          <el-button type="primary" :loading="sending" @click="onSend">发送</el-button>
        </div>
      </el-footer>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted, nextTick, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { listConversations, createConversation, listMessages, sendMessage } from '../api/chat';
import { useUserStore } from '../stores/user';
import { useRouter } from 'vue-router';

const router = useRouter();
const store = useUserStore();
const conversations = ref([]);
const messages = ref([]);
const activeId = ref(null);
const input = ref('');
const sending = ref(false);
const scrollRef = ref();

const activeConversationTitle = computed(() => {
  const c = conversations.value.find(c => c.id === activeId.value);
  return c ? c.title : '请选择会话';
});

const loadConversations = async () => {
  const res = await listConversations();
  conversations.value = res.data;
  if (!activeId.value && conversations.value.length) {
    activeId.value = conversations.value[0].id;
    await loadMessages();
  }
};

const loadMessages = async () => {
  if (!activeId.value) return;
  const res = await listMessages(activeId.value);
  messages.value = res.data;
  await nextTick();
  scrollToBottom();
};

const newConversation = async () => {
  const title = await ElMessageBox.prompt('输入对话标题', '新建对话', { inputValue: '新的对话' }).then(r => r.value).catch(() => null);
  if (!title) return;
  const res = await createConversation({ title });
  conversations.value.unshift(res.data);
  activeId.value = res.data.id;
  messages.value = [];
};

const switchConversation = async (id) => {
  activeId.value = id;
  await loadMessages();
};

const onSend = async () => {
  if (!input.value.trim() || !activeId.value) return;
  const content = input.value;
  input.value = '';
  messages.value.push({ role: 'user', content });
  await nextTick();
  scrollToBottom();
  sending.value = true;
  try {
    const res = await sendMessage(activeId.value, { content });
    messages.value.push(res.data);
    await nextTick();
    scrollToBottom();
  } finally {
    sending.value = false;
  }
};

const scrollToBottom = () => {
  const wrap = scrollRef.value?.$el.querySelector('.el-scrollbar__wrap');
  if (wrap) wrap.scrollTop = wrap.scrollHeight;
};

const logout = () => {
  store.logout();
  router.push('/login');
};

const goUsers = () => router.push('/users');

onMounted(async () => {
  await loadConversations();
});
</script>

<style scoped>
.chat-page { height: 100vh; }
.sidebar { background: #fff; border-right: 1px solid #e5e6eb; }
.sidebar-header { display:flex; justify-content:space-between; align-items:center; padding:12px; }
.logo { font-weight:700; }
.conversation-menu { border-right:none; }
.topbar { display:flex; justify-content:space-between; align-items:center; background:#fff; border-bottom:1px solid #e5e6eb; }
.chat-body { background:#f6f7fb; padding:0; }
.messages { height: calc(100vh - 200px); padding:16px; }
.msg { margin-bottom:12px; }
.msg .bubble { max-width: 70%; padding:10px 12px; border-radius:10px; }
.msg.user { text-align:right; }
.msg.user .bubble { margin-left:auto; background:#4b7bec; color:#fff; }
.msg.assistant .bubble { background:#fff; border:1px solid #e5e6eb; }
.role { font-size:12px; color:#999; margin-bottom:4px; }
.chat-input { background:#fff; border-top:1px solid #e5e6eb; padding:12px; }
.input-actions { margin-top:8px; text-align:right; }
</style>
