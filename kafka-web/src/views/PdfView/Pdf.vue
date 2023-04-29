<template>
  <div :loading="loading">
    <div class="button-class">
      <el-button type="primary" @click="downloadPdf"> Download </el-button>
      <el-button type="primary" @click="sendMail"> Send by Email </el-button>
      <el-col :span="4">
        <el-input v-model="mail" width="200px" />
      </el-col>
    </div>
    <vue-pdf-embed
      :source="url"
      class="vue-pdf-embed"
      v-for="i in pdfPageNum"
      :key="i"
      :page="i"
      width="1200"
    />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, reactive } from 'vue'
import { useRoute } from 'vue-router'

import { getPdfApi, sendMailApi } from '@/api/test'
import type { SendMailRequest } from '@/api/test/types'

import VuePdfEmbed from 'vue-pdf-embed'
import { createLoadingTask } from 'vue3-pdfjs/esm'
import { ElButton, ElInput, ElCol } from 'element-plus'

const router = useRoute()

const loading = ref(false)

const pdfId = router.query.id as string
const pdfType = router.query.type as string

const mail = ref('')

const sendMailRequest = reactive<SendMailRequest>({
  toMail: '',
  fileName: ''
})

const pdfName = ref('')
const url = ref('')
const pdfPageNum = ref(0)

const loadPdf = async () => {
  const res = await getPdfApi({
    type: pdfType,
    id: pdfId
  }).catch(() => {
    alert('error')
  })
  // console.log('res', res)
  if (res) {
    url.value = window.URL.createObjectURL(res.data)
    pdfName.value = getFileName(pdfId)
    // console.log(pdfName.value)
  }
}

const getFileName = (id: string) => {
  const name = ref('phone-advice-' + id + '.pdf')
  return name.value
}

const downloadPdf = () => {
  const a = document.createElement('a')
  a.href = url.value
  a.style.display = 'none'
  document.body.appendChild(a)
  a.click()
  document.body.removeChild(a)
}

const sendMail = async () => {
  sendMailRequest.toMail = mail.value
  sendMailRequest.fileName = pdfName.value
  console.log('sendMailRequest', sendMailRequest)
  await sendMailApi(sendMailRequest)
}

onMounted(() => {
  // console.log(router.query)

  loadPdf().then(() => {
    loading.value = true
    const loadingTask = createLoadingTask(url.value)
    loadingTask.promise.then((pdf: { numPages: number }) => {
      pdfPageNum.value = pdf.numPages
      loading.value = false
    })
  })
})
</script>

<style scoped>
.vue-pdf-embed {
  text-align: center;
}

.button-class {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
