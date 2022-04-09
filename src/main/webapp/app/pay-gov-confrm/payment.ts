export interface IPayment {
  id?: number;
  cik?: string;
  ccc?: number;
  paymentAmount?: number;
  name?: string;
  email?: string;
  phone?: string | null;
}

export interface Ipay {
  RETURNMAC?: string;
  hostedCheckoutId?: string;
  invalidTokens?: any;
  merchantReference?: string;
  partialRedirectUrl?: string;
}

export interface Imock {
  track_id?: string;
  name?: string;
  email?: string;
  address?: string;
  creditCard?: number;
}

export class pay implements Ipay {
  constructor(
    public RETURNMAC?: string,
    public hostedCheckoutId?: string,
    public invalidTokens?: any,
    public merchantReference?: string,
    public partialRedirectUrl?: string
  ) {}
}

export class Mock implements Imock {
  constructor(public track_id?: string, public name?: string, public email?: string, public address?: string, public creditCard?: number) {}
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public cik?: string,
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
