export interface IPayment {
  id?: number;
  cik?: number;
  ccc?: number;
  paymentAmount?: number;
  name?: string;
  email?: string;
  phone?: string | null;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public cik?: number,
    public ccc?: number,
    public paymentAmount?: number,
    public name?: string,
    public email?: string,
    public phone?: string | null
  ) {}
}

export function getPaymentIdentifier(payment: IPayment): number | undefined {
  return payment.id;
}
