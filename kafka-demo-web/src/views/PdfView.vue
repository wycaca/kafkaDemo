<template>
  <div :loading="loading">
    <div class="button-class">
      <el-button type="primary"> Download </el-button>
      <el-button type="primary"> Send by Email </el-button>
    </div>
    <vue-pdf-embed :source="url" class="vue-pdf-embed" v-for="i in pdfPageNum" :page="i" width="1200" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

import { getPdfApi } from '@/api/test'

import VuePdfEmbed from 'vue-pdf-embed'
import { createLoadingTask } from 'vue3-pdfjs'

const router = useRoute()

const loading = ref(false)

const pdfId = router.query.id as string
const pdfType = router.query.type as string

const pdfName = ref('')
const url = ref('')
const pdfPageNum = ref(0)

const loadPdf = async () => {
  const res = await getPdfApi({
    type: pdfType,
    id: pdfId
  })
    .catch(() => { })
    .finally(() => { })
  // console.log("res", res)
  if (res) {
    url.value = window.URL.createObjectURL(res.data)
    pdfName.value = getFileName(res.headers['content-disposition'])
    console.log(pdfName.value)
  }
}

const getFileName = (url : string) => {
  const name = ref('');
  if (url !== null && url !== "") {
    name.value = url.substring(url.lastIndexOf("/") + 1).replaceAll('\"', '');
  } else {
    name.value = url;
  }
  return name.value;
}

onMounted(() => {
  console.log(router.query);

  const res = loadPdf().then(() => {
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
