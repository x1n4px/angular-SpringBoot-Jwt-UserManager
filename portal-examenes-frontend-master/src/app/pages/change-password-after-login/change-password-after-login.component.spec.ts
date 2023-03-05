import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangePasswordAfterLoginComponent } from './change-password-after-login.component';

describe('ChangePasswordAfterLoginComponent', () => {
  let component: ChangePasswordAfterLoginComponent;
  let fixture: ComponentFixture<ChangePasswordAfterLoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChangePasswordAfterLoginComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChangePasswordAfterLoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
