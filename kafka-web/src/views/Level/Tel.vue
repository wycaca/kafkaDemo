<template>
  <el-form label-width="200px" :model="user" :rules="rules" ref="editForm" class="edit-form-class">
    <el-form-item label="Name">
      <el-col :span="8">
        <el-input v-model="user.id" />
      </el-col>
    </el-form-item>
    <el-form-item label="Phone">
      <el-col :span="8">
        <el-input v-model="user.tel" />
      </el-col>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm" :loading="loading"> Modifty </el-button>
      <el-button type="primary" @click="checkPdf" :loading="pdfLoading"> CheckPDF </el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { reactive, ref, unref } from 'vue'
import { useRouter } from 'vue-router'
import { editApi } from '@/api/test'
import type { EditMsg } from '@/api/test/types'
import { ElForm, ElFormItem, ElButton, ElInput, ElCol } from 'element-plus'
import type { FormInstance } from 'element-plus'

const { push } = useRouter()

const editForm = ref<FormInstance>()
const loading = ref(false)
const pdfLoading = ref(false)
const user = reactive<EditMsg>({
  id: '123',
  tel: '123456789012',
  email: '123@mail.com'
})

const rules = {
  //验证邮箱
  email: [
    {
      pattern:
        /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
      message: 'Please enter email',
      trigger: ['blur', 'change']
    }
  ]
  // 验证手机号
  // telephone: [
  //   { required: true, message: "Can't be empty", trigger: 'blur' },
  //   { pattern: /^[0-9]{12}$/, message: 'Please enter phone number', trigger: 'blur' }
  // ]
}

const submitForm = async () => {
  const form = unref(editForm)
  await form?.validate(async (isValid) => {
    if (isValid) {
      loading.value = true
      const res = await editApi(user)
        .catch(() => {})
        .finally(() => (loading.value = false))
      if (res) {
      }
    }
  })
}

const checkPdf = () => {
  push({
    path: '/pdf',
    query: {
      type: 'phone',
      id: user.id
    }
  })
}

// const checkPdf2 = async () => {
//   pdfLoading.value = true
//   const res = await getPdfApi({
//     type: 'phone',
//     id: user.id
//   })
//     .catch(() => {})
//     .finally(() => (pdfLoading.value = false))
//   console.log("res", res) // for debug
//     if (res) {
//     const pdfUrl = window.URL.createObjectURL(res.data)
//     window.open(pdfUrl)
//   }
// }
</script>

<style scoped></style>
