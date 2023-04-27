import { config } from '@/config/axios/config'
import { MockMethod } from 'vite-plugin-mock'

const { result_code } = config

const timeout = 1000

const adminList = [
  {
    path: '/telForm',
    component: 'views/Level/Tel.vue',
    redirect: '/telForm',
    name: 'TelForm',
    meta: {
      title: 'Modify Tel'
    }
  }
]

const testList: string[] = [
  '/telForm',
  '/pdf'
]

export default [
  // 列表接口
  {
    url: '/role/list',
    method: 'get',
    timeout,
    response: ({ query }) => {
      const { roleName } = query
      return {
        code: result_code,
        data: {
          list: roleName === 'admin' ? adminList : testList
        }
      }
    }
  }
] as MockMethod[]
