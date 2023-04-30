<template>
  <div :loading="loading">
    <div class="button-class">
      <el-button type="primary" @click="downloadPdf"> Download </el-button>
      <el-button type="primary" @click="sendMail"> Send by Email </el-button>
      <el-col :span="4">
        <el-input v-model="mail" width="200px" />
      </el-col>
    </div>

    <VuePdf class="vue-pdf-embed" v-for="page in pdfPageNum" :key="page" :src="url" :page="page" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, reactive } from 'vue'
import { useRoute } from 'vue-router'
import { ElButton, ElInput, ElCol } from 'element-plus'

import { VuePdf, createLoadingTask } from 'vue3-pdfjs'

import { getPdfApi, sendMailApi } from '@/api/test'
import type { SendMailRequest } from '@/api/test/types'

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
  // const res = await getPdfApi({
  //   type: pdfType,
  //   id: pdfId
  // }).catch(() => {
  //   alert('error')
  // })
  // // console.log('res', res)
  // if (res) {
  //   const binaryData = []
  //   binaryData.push(res.data)
  //   //获取blob链接
  //   url.value = window.URL.createObjectURL(new Blob(binaryData, { type: 'application/pdf' }))
  //   console.log('url', url)
  //   // url.value = window.URL.createObjectURL(res.data)
  //   pdfName.value = getFileName(pdfId)
  //   // console.log(pdfName.value)
  // }

  url.value = "http://线上应用地址/test/pdf?type=phone&id=" + pdfId
  pdfName.value = getFileName(pdfId)
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
    loadingTask.promise.then((pdf: { numPages }) => {
      pdfPageNum.value = parseInt(pdf.numPages)
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
