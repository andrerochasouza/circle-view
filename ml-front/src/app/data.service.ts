import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  private storage: Storage;

  constructor() {
    this.storage = window.localStorage;
   }

  set(key: string, value: string) {
    this.storage.setItem(key, value);
  }

  get(key: string): any {
      return this.storage.getItem(key);
  }

  remove(key: string): boolean {
    if (this.storage) {
      this.storage.removeItem(key);
      return true;
    }
    return false;
  }
}