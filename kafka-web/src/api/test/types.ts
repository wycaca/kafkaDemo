export type EditMsg = {
  id: string
  tel: string
  email: string
}

export type PDFRequest = {
  type: string
  id: string
}

export type SendMailRequest = {
  toMail: string
  fileName: string
}
