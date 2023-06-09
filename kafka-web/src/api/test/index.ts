import request from '@/config/axios'
import type { AxiosResponse } from 'axios'
import type { EditMsg, PDFRequest, SendMailRequest } from './types'

export const editApi = (data: EditMsg): Promise<IResponse> => {
  return request.post({
    url: '/test/send',
    data
  })
}

export const getPdfApi = (params: PDFRequest): Promise<AxiosResponse> => {
  return request.get({
    url: '/test/pdf',
    params,
    headersType: 'application/x-www-form-urlencoded',
    responseType: 'blob'
  })
}

export const sendMailApi = (data: SendMailRequest): Promise<IResponse> => {
  return request.post({
    url: '/test/mail/send',
    data
  })
}
