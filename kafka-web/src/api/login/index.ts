import request from '@/config/axios'
import type { UserLoginType, UserType } from './types'

export const loginApi = (data: UserLoginType) => {
  return request.post({
    url: '/user/login',
    data
  })
}

export const loginOutApi = () => {
  return request.get({ url: '/user/loginOut' })
}

export const getUserListApi = ({ params }: AxiosConfig) => {
  return request.get<{
    total: number
    list: UserType[]
  }>({ url: '/user/list', params })
}

export const getAdminRoleApi = ({ params }) => {
  return request.get<{
    list: AppCustomRouteRecordRaw[]
  }>({ url: '/role/list', params })
}

export const getTestRoleApi = ({ params }) => {
  return request.get<{
    list: string[]
  }>({ url: '/role/list', params })
}
